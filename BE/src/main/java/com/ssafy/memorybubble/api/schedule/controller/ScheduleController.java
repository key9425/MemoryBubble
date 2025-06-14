package com.ssafy.memorybubble.api.schedule.controller;

import com.ssafy.memorybubble.api.schedule.dto.LinkRequest;
import com.ssafy.memorybubble.api.schedule.dto.ScheduleRequest;
import com.ssafy.memorybubble.api.schedule.dto.ScheduleResponse;
import com.ssafy.memorybubble.common.exception.ErrorResponse;
import com.ssafy.memorybubble.api.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Schedule Controller", description = "일정 관련 Controller 입니다.")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(
            summary = "일정 추가 API",
            description = "body에 일정 정보를 전달하고 일정을 생성합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "해당 가족에 가입되어 있지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 앨범에 접근할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 날짜입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<ScheduleResponse> addSchedule(@AuthenticationPrincipal UserDetails userDetails,
                                                        @Valid @RequestBody ScheduleRequest request) {
        ScheduleResponse scheduleResponse = scheduleService.addSchedule(Long.valueOf(userDetails.getUsername()), request);
        return ResponseEntity.ok().body(scheduleResponse);
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(
            summary = "일정 삭제 API",
            description = "일정 id로 일정을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 가족에 가입되어 있지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<Void> deleteSchedule(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable("scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(Long.valueOf(userDetails.getUsername()), scheduleId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{scheduleId}")
    @Operation(
            summary = "일정 수정 API",
            description = "일정 id로 일정을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 가족에 가입되어 있지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 날짜입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<ScheduleResponse> updateSchedule(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable Long scheduleId,
                                                           @Valid @RequestBody ScheduleRequest request) {
        ScheduleResponse scheduleResponse = scheduleService.updateSchedule(Long.valueOf(userDetails.getUsername()), scheduleId, request);
        return ResponseEntity.ok().body(scheduleResponse);
    }

    @PostMapping("/{scheduleId}/link")
    @Operation(
            summary = "일정과 앨범 연결 API",
            description = "일정 id와 앨범 id로 일정을 앨범에 연결합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 가족에 가입되어 있지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 앨범에 접근할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<ScheduleResponse> linkSchedule(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable Long scheduleId,
                                                         @Valid @RequestBody LinkRequest request) {
        ScheduleResponse scheduleResponse = scheduleService.linkSchedule(Long.valueOf(userDetails.getUsername()), scheduleId, request.getAlbumId());
        return ResponseEntity.ok().body(scheduleResponse);
    }

    @GetMapping
    @Operation(
            summary = "일정 목록 API",
            description = "해당 년도, 월에 포함되어 있는 가족 일정 목록을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공(일정 목록)"),
                    @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 가족에 가입되어 있지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    public ResponseEntity<List<ScheduleResponse>> getSchedules(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestParam("family_id") Long familyId,
                                                               @RequestParam("year") int year,
                                                               @RequestParam("month") int month) {
        List<ScheduleResponse> schedules = scheduleService.getSchedules(Long.valueOf(userDetails.getUsername()), familyId, year, month);
        return ResponseEntity.ok().body(schedules);
    }
}