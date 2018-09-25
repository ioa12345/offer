package offer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import offer.ErrorDetails;
import offer.model.Offer;
import offer.model.Price;
import offer.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Validator validator;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createOffer_success() throws Exception {
        Offer offer = TestUtils.createOffer("offer1");
        String response = postOffer(offer);

        assertThat(response, is("Offer created"));
    }


    @Test
    public void createOffer_shouldThrowValidationMessageWhenMandatoryFieldsAreMissing() throws Exception {

        Offer offer = new Offer();
        String badResponse = this.mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorDetails errorDetails = objectMapper.readValue(badResponse, ErrorDetails.class);

        assertThat(errorDetails.getTag(), is("VALIDATION_ERROR"));
        List<String> errorDetailsMessage = errorDetails.getMessage();
        assertThat(errorDetailsMessage, hasItem("validityTime must not be null"));
        assertThat(errorDetailsMessage, hasItem("price must not be null"));
        assertThat(errorDetailsMessage, hasItem("description must not be null"));
    }


    @Test
    public void createOffer_shouldThrowValidationMessageWhenInvalidPrice() throws Exception {
        Offer offer = new Offer();
        offer.setId("offer1");
        offer.setCancelled(false);
        offer.setDescription("Offer 1 description");
        offer.setStartDate(LocalDate.now().plusMonths(3).format(DateTimeFormatter.ISO_DATE));
        offer.setValidityTime(1000000);
        offer.setPrice(new Price());

        String badResponse = this.mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorDetails errorDetails = objectMapper.readValue(badResponse, ErrorDetails.class);

        assertThat(errorDetails.getTag(), is("VALIDATION_ERROR"));
        List<String> errorDetailsMessage = errorDetails.getMessage();
        assertThat(errorDetailsMessage, hasItem("price.currency must not be null"));
        assertThat(errorDetailsMessage, hasItem("price.amount must not be null"));
    }

    @Test
    public void createOffer_shouldThrowValidationMessageWhenDateIsInThePast() throws Exception {
        Offer offer = new Offer();
        offer.setId("offer1");
        offer.setCancelled(false);
        offer.setDescription("Offer 1 description");
        offer.setStartDate(LocalDate.now().minusMonths(3).format(DateTimeFormatter.ISO_DATE));
        offer.setValidityTime(1000000);
        Price price = new Price();
        price.setAmount(new BigDecimal(10));
        price.setCurrency("GBP");
        offer.setPrice(price);

        String badResponse = this.mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorDetails errorDetails = objectMapper.readValue(badResponse, ErrorDetails.class);

        assertThat(errorDetails.getTag(), is("VALIDATION_ERROR"));
        List<String> errorDetailsMessage = errorDetails.getMessage();
        assertThat(errorDetailsMessage, hasItem("startDate cannot be in the past"));

    }

    @Test
    public void retrieveOffer_success() throws Exception {
        Offer offer = TestUtils.createOffer("offer1");
        postOffer(offer);

        String response = this.mockMvc.perform(get("/retrieve/offer1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response, is(objectMapper.writeValueAsString(offer)));
    }

    @Test
    public void retrieveOffer_shouldFailWhenOfferIsExpired() throws Exception {
        Offer offer = TestUtils.createOffer("offer1");
        offer.setCancelled(true);
        postOffer(offer);

        String badResponse = this.mockMvc.perform(get("/retrieve/offer1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorDetails errorDetails = objectMapper.readValue(badResponse, ErrorDetails.class);

        assertThat(errorDetails.getTag(), is("OFFER_EXPIRED"));
        List<String> errorDetailsMessage = errorDetails.getMessage();
        assertThat(errorDetails.getMessage().size(), is(1));
        assertThat(errorDetailsMessage, hasItem("Offer is already cancelled"));
    }

    @Test
    public void retrieveOffer_shouldFailWhenOfferJustExpired() throws Exception {
        Offer offer = TestUtils.createOffer("offer1");
        offer.setStartDate(null);
        offer.setValidityTime(-1);
        postOffer(offer);

        String badResponse = this.mockMvc.perform(get("/retrieve/offer1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        ErrorDetails errorDetails = objectMapper.readValue(badResponse, ErrorDetails.class);

        assertThat(errorDetails.getTag(), is("OFFER_EXPIRED"));
        List<String> errorDetailsMessage = errorDetails.getMessage();
        assertThat(errorDetails.getMessage().size(), is(1));
        assertThat(errorDetailsMessage, hasItem("Offer has expired"));
    }

    @Test
    public void retrieveOffer_notFound() throws Exception {
        String badResponse = this.mockMvc.perform(get("/retrieve/offer2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        ErrorDetails errorDetails = objectMapper.readValue(badResponse, ErrorDetails.class);

        assertThat(errorDetails.getTag(), is("OFFER_NOT_FOUND"));
        List<String> errorDetailsMessage = errorDetails.getMessage();
        assertThat(errorDetails.getMessage().size(), is(1));
        assertThat(errorDetailsMessage, hasItem("Offer does not exist"));
    }

    @Test
    public void cancelOffer_success() throws Exception {
        Offer offer = TestUtils.createOffer("offer1");
        postOffer(offer);

        String response = this.mockMvc.perform(put("/cancel/offer1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response, is("Succesfully Cancelled"));

    }

    @Test
    public void cancelOffer_shouldFailWhenOfferDoesNotExist() throws Exception {

        String badResponse = this.mockMvc.perform(put("/cancel/offer2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        ErrorDetails errorDetails = objectMapper.readValue(badResponse, ErrorDetails.class);

        assertThat(errorDetails.getTag(), is("OFFER_NOT_FOUND"));
        List<String> errorDetailsMessage = errorDetails.getMessage();
        assertThat(errorDetails.getMessage().size(), is(1));
        assertThat(errorDetailsMessage, hasItem("Cannot cancel an offer which does not exist"));

    }

    private String postOffer(Offer offer) throws Exception {
        return this.mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offer)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}
