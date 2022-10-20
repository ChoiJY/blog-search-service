package io.github.choijy.statisticsservice.controller;

import static java.nio.charset.StandardCharsets.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.github.choijy.statisticsservice.domain.SearchHistory;
import io.github.choijy.statisticsservice.service.StatisticsService;

/**
 * Description : Statistics Controller test class.
 * <p>
 * Created by jychoi on 2022/09/19.
 */
@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @DisplayName("인기 검색어 top 10 조회")
    @Nested
    class GetBlogSearchTop10Test {

        @DisplayName("성공 - 정상 조회")
        @Test
        void getBlogSearchTop10() throws Exception {
            List<SearchHistory> result = new ArrayList<>();
            result.add(new SearchHistory("블로그", 322L));
            result.add(new SearchHistory("카카오", 300L));
            result.add(new SearchHistory("오늘의 날씨", 33L));
            result.add(new SearchHistory("네이버", 3L));

            when(statisticsService.getSearchHistoryTop10()).thenReturn(result);

            MvcResult mvcResult = mockMvc.perform(get("/v1/statistics/blog-search/top10"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString(UTF_8)).isEqualTo(
                    "{\"code\":200,\"responses\":[{\"keyword\":\"블로그\",\"count\":322},{\"keyword\":\"카카오\",\"count\":300},{\"keyword\":\"오늘의 날씨\",\"count\":33},{\"keyword\":\"네이버\",\"count\":3}]}");
        }

        @DisplayName("실패 - 결과가 없는 경우")
        @Test
        void getBlogSearchTop10NoData() throws Exception {
            when(statisticsService.getSearchHistoryTop10()).thenReturn(Collections.emptyList());

            MvcResult mvcResult = mockMvc.perform(get("/v1/statistics/blog-search/top10"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString(UTF_8)).isEqualTo("{\"code\":200,\"responses\":[]}");
        }

        @DisplayName("실패 - 조회 중 Exception 발생")
        @Test
        void getBlogSearchTop10Failed() throws Exception {
            when(statisticsService.getSearchHistoryTop10()).thenThrow(new RuntimeException("Exception occurred."));

            MvcResult mvcResult = mockMvc.perform(get("/v1/statistics/blog-search/top10"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString(UTF_8)).isEqualTo(
                    "{\"code\":400,\"message\":\"Exception occurred.\"}");
        }
    }

    @DisplayName("검색 키워드 기록 count 업데이트")
    @Nested
    class UpdateSearchHistoryTest {

        @DisplayName("성공 - 정상 업데이트")
        @Test
        void updateSearchHistory() throws Exception {
            String keyword = "네이버";
            doNothing().when(statisticsService).increaseSearchHistoryCount(keyword);

            mockMvc.perform(put("/v1/statistics/blog-search/" + keyword))
                    .andExpect(status().isOk());

            verify(statisticsService, times(1)).increaseSearchHistoryCount(keyword);
        }

        @DisplayName("실패 - 업데이트 시 Keyword 누락")
        @Test
        void updateSearchHistoryWithoutKeyword() throws Exception {
            String keyword = "네이버";
            doThrow(new IllegalArgumentException()).when(statisticsService).increaseSearchHistoryCount(keyword);

            MvcResult mvcResult = mockMvc.perform(put("/v1/statistics/blog-search/"))
                    .andExpect(status().isNotFound())
                    .andReturn();

            verify(statisticsService, never()).increaseSearchHistoryCount(keyword);
            assertThat(mvcResult.getResponse().getContentAsString(UTF_8)).isEqualTo("{\"code\":404,\"message\":\"No handler found for PUT /v1/statistics/blog-search/\"}");
        }

        @DisplayName("실패 - 업데이트 시 Exception 발생")
        @Test
        void updateSearchHistoryFailed() throws Exception {
            String keyword = "네이버";
            doThrow(new RuntimeException("Exception occurred.")).when(statisticsService).increaseSearchHistoryCount(keyword);

            MvcResult mvcResult = mockMvc.perform(put("/v1/statistics/blog-search/" + keyword))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            verify(statisticsService, times(1)).increaseSearchHistoryCount(keyword);
            assertThat(mvcResult.getResponse().getContentAsString(UTF_8)).isEqualTo("{\"code\":400,\"message\":\"Exception occurred.\"}");
        }
    }
}