/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.dspace.app.rest.matcher.VocabularyEntryDedailsMatcher;
import org.dspace.app.rest.test.AbstractControllerIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 
 * 
 * @author Mykhaylo Boychuk (4science.it)
 */
public class VocabularyEntryDetailsIT extends AbstractControllerIntegrationTest {

    @Test
    public void findAllTest() throws Exception {
        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken).perform(get("/api/submission/vocabularyEntryDetails"))
                            .andExpect(status()
                            .isMethodNotAllowed());
    }

    @Test
    public void findOneTest() throws Exception {
        String idAuthority = "srsc:SCB110";
        String token = getAuthToken(eperson.getEmail(), password);
        getClient(token).perform(get("/api/submission/vocabularyEntryDetails/" + idAuthority))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is("srsc:SCB110")))
                        .andExpect(jsonPath("$.value",
                                         is("Research Subject Categories::HUMANITIES and RELIGION::Religion/Theology")))
                        .andExpect(jsonPath("$.display", is("Religion/Theology")))
                        .andExpect(jsonPath("$.selectable", is(true)))
                        .andExpect(jsonPath("$.otherInformation.id", is("SCB110")))
                        .andExpect(jsonPath("$.otherInformation.note", is("Religionsvetenskap/Teologi")))
                        .andExpect(jsonPath("$.otherInformation.parent", is("HUMANITIES and RELIGION")));
    }

    @Test
    public void findOneUnauthorizedTest() throws Exception {
        String idAuthority = "srsc:SCB110";
        getClient().perform(get("/api/submission/vocabularyEntryDetails/" + idAuthority))
                   .andExpect(status().isUnauthorized());
    }

    @Test
    public void srscSearchTopTest() throws Exception {
        String tokenAdmin = getAuthToken(admin.getEmail(), password);
        String tokenEPerson = getAuthToken(eperson.getEmail(), password);
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/search/top")
          .param("vocabulary", "srsc"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$._embedded.vocabularyEntryDetails", Matchers.containsInAnyOrder(
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB11", "HUMANITIES and RELIGION"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB12", "LAW/JURISPRUDENCE"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB13", "SOCIAL SCIENCES"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB14", "MATHEMATICS"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB15", "NATURAL SCIENCES"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB16", "TECHNOLOGY"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB17",
                                       "FORESTRY, AGRICULTURAL SCIENCES and LANDSCAPE PLANNING"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB18", "MEDICINE"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB19", "ODONTOLOGY"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB21", "PHARMACY"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB22", "VETERINARY MEDICINE"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB23", "INTERDISCIPLINARY RESEARCH AREAS")
          )))
          .andExpect(jsonPath("$.page.totalElements", Matchers.is(12)));

        getClient(tokenEPerson).perform(get("/api/submission/vocabularyEntryDetails/search/top")
         .param("vocabulary", "srsc"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$._embedded.vocabularyEntryDetails", Matchers.containsInAnyOrder(
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB11", "HUMANITIES and RELIGION"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB12", "LAW/JURISPRUDENCE"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB13", "SOCIAL SCIENCES"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB14", "MATHEMATICS"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB15", "NATURAL SCIENCES"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB16", "TECHNOLOGY"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB17",
                                       "FORESTRY, AGRICULTURAL SCIENCES and LANDSCAPE PLANNING"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB18", "MEDICINE"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB19", "ODONTOLOGY"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB21", "PHARMACY"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB22", "VETERINARY MEDICINE"),
          VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB23", "INTERDISCIPLINARY RESEARCH AREAS")
          )))
         .andExpect(jsonPath("$.page.totalElements", Matchers.is(12)));
    }

    @Test
    public void srscSearchFirstLevel_MATHEMATICS_Test() throws Exception {
        String tokenAdmin = getAuthToken(admin.getEmail(), password);
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/srsc:SCB14/children"))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$._embedded.children", Matchers.containsInAnyOrder(
                   VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB1401",
                                                        "Algebra, geometry and mathematical analysis"),
                   VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB1402", "Applied mathematics"),
                   VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB1409", "Other mathematics")
                  )))
                 .andExpect(jsonPath("$._embedded.children[0].otherInformation.parent",
                                  is("Research Subject Categories::MATHEMATICS")))
                 .andExpect(jsonPath("$.page.totalElements", Matchers.is(3)));
    }

    @Test
    public void srscSearchTopPaginationTest() throws Exception {
        String tokenAdmin = getAuthToken(admin.getEmail(), password);
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/search/top")
                             .param("vocabulary", "srsc")
                             .param("page", "0")
                             .param("size", "5"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$._embedded.vocabularyEntryDetails", Matchers.containsInAnyOrder(
                  VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB11", "HUMANITIES and RELIGION"),
                  VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB12", "LAW/JURISPRUDENCE"),
                  VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB13", "SOCIAL SCIENCES"),
                  VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB14", "MATHEMATICS"),
                  VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB15", "NATURAL SCIENCES")
              )))
          .andExpect(jsonPath("$.page.totalElements", is(12)))
          .andExpect(jsonPath("$.page.totalPages", is(3)))
          .andExpect(jsonPath("$.page.number", is(0)));

        //second page
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/search/top")
                 .param("vocabulary", "srsc")
                 .param("page", "1")
                 .param("size", "5"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.vocabularyEntryDetails", Matchers.containsInAnyOrder(
           VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB16", "TECHNOLOGY"),
           VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB17",
                        "FORESTRY, AGRICULTURAL SCIENCES and LANDSCAPE PLANNING"),
           VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB18", "MEDICINE"),
           VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB19", "ODONTOLOGY"),
           VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB21", "PHARMACY")
               )))
           .andExpect(jsonPath("$.page.totalElements", is(12)))
           .andExpect(jsonPath("$.page.totalPages", is(3)))
           .andExpect(jsonPath("$.page.number", is(1)));

        // third page
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/search/top")
                 .param("vocabulary", "srsc")
                 .param("page", "2")
                 .param("size", "5"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.vocabularyEntryDetails", Matchers.containsInAnyOrder(
                   VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB22", "VETERINARY MEDICINE"),
                   VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB23", "INTERDISCIPLINARY RESEARCH AREAS")
               )))
           .andExpect(jsonPath("$.page.totalElements", is(12)))
           .andExpect(jsonPath("$.page.totalPages", is(3)))
           .andExpect(jsonPath("$.page.number", is(2)));
    }

    @Test
    public void searchTopBadRequestTest() throws Exception {
        String tokenAdmin = getAuthToken(admin.getEmail(), password);
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/search/top")
                             .param("vocabulary", UUID.randomUUID().toString()))
                             .andExpect(status().isBadRequest());
    }

    @Test
    public void searchTopUnauthorizedTest() throws Exception {
        getClient().perform(get("/api/submission/vocabularyEntryDetails/search/top")
                   .param("vocabulary", "srsc:SCB16"))
                   .andExpect(status().isUnauthorized());
    }

    @Test
    public void srscSearchByParentFirstLevelPaginationTest() throws Exception {
        String token = getAuthToken(eperson.getEmail(), password);
        // first page
        getClient(token).perform(get("/api/submission/vocabularyEntryDetails/srsc:SCB14/children")
                 .param("page", "0")
                 .param("size", "2"))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$._embedded.children", Matchers.containsInAnyOrder(
                     VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB1401",
                                                     "Algebra, geometry and mathematical analysis"),
                     VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB1402", "Applied mathematics")
                     )))
                 .andExpect(jsonPath("$.page.totalElements", is(3)))
                 .andExpect(jsonPath("$.page.totalPages", is(2)))
                 .andExpect(jsonPath("$.page.number", is(0)));

        // second page
        getClient(token).perform(get("/api/submission/vocabularyEntryDetails/srsc:SCB14/children")
                .param("page", "1")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.children", Matchers.contains(
                    VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB1409", "Other mathematics")
                    )))
                .andExpect(jsonPath("$.page.totalElements", is(3)))
                .andExpect(jsonPath("$.page.totalPages", is(2)))
                .andExpect(jsonPath("$.page.number", is(1)));
    }

    @Test
    public void srscSearchByParentSecondLevel_Applied_mathematics_Test() throws Exception {
        String token = getAuthToken(eperson.getEmail(), password);
        getClient(token).perform(get("/api/submission/vocabularyEntryDetails/srsc:SCB1402/children"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$._embedded.children", Matchers.containsInAnyOrder(
                          VocabularyEntryDedailsMatcher.matchAuthority("srsc:VR140202", "Numerical analysis"),
                          VocabularyEntryDedailsMatcher.matchAuthority("srsc:VR140203", "Mathematical statistics"),
                          VocabularyEntryDedailsMatcher.matchAuthority("srsc:VR140204", "Optimization, systems theory"),
                          VocabularyEntryDedailsMatcher.matchAuthority("srsc:VR140205", "Theoretical computer science")
                         )))
                        .andExpect(jsonPath("$.page.totalElements", Matchers.is(4)));
    }

    @Test
    public void srscSearchByParentEmptyTest() throws Exception {
        String tokenAdmin = getAuthToken(admin.getEmail(), password);
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/srsc:VR140202/children"))
                             .andExpect(status().isOk())
                             .andExpect(jsonPath("$.page.totalElements", Matchers.is(0)));
    }

    @Test
    public void srscSearchByParentWrongIdTest() throws Exception {
        String tokenAdmin = getAuthToken(admin.getEmail(), password);
        getClient(tokenAdmin).perform(get("/api/submission/vocabularyEntryDetails/"
                                                          + UUID.randomUUID() + "/children"))
                             .andExpect(status().isBadRequest());
    }

    @Test
    public void srscSearchTopUnauthorizedTest() throws Exception {
        getClient().perform(get("/api/submission/vocabularyEntryDetails/search/top")
                   .param("vocabulary", "srsc"))
                   .andExpect(status().isUnauthorized());
    }

    @Test
    public void findParentByChildTest() throws Exception {
        String tokenEperson = getAuthToken(eperson.getEmail(), password);
        getClient(tokenEperson).perform(get("/api/submission/vocabularyEntryDetails/srsc:SCB11/parent"))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$", is(VocabularyEntryDedailsMatcher.matchAuthority(
                                     "srsc:ResearchSubjectCategories", "Research Subject Categories")
                  )));
    }

    @Test
    public void findParentByChildSecondLevelTest() throws Exception {
        String tokenEperson = getAuthToken(eperson.getEmail(), password);
        getClient(tokenEperson).perform(get("/api/submission/vocabularyEntryDetails/srsc:SCB180/parent"))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$", is(
                         VocabularyEntryDedailsMatcher.matchAuthority("srsc:SCB18", "MEDICINE")
                  )));
    }

    @Test
    public void findParentByChildBadRequestTest() throws Exception {
        String tokenEperson = getAuthToken(eperson.getEmail(), password);
        getClient(tokenEperson).perform(get("/api/submission/vocabularyEntryDetails/" + UUID.randomUUID() + "/parent"))
                               .andExpect(status().isBadRequest());
    }

    @Test
    public void findParentByChildUnauthorizedTest() throws Exception {
        getClient().perform(get("/api/submission/vocabularyEntryDetails/srsc:SCB180/parent"))
                   .andExpect(status().isUnauthorized());
    }

    @Test
    public void findParentRootChildTest() throws Exception {
        String tokenEperson = getAuthToken(eperson.getEmail(), password);
        getClient(tokenEperson).perform(get("/api/submission/vocabularyEntryDetails/srsc/parent"))
                               .andExpect(status().isNoContent());
    }
}
