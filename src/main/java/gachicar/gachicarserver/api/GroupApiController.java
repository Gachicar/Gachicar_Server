package gachicar.gachicarserver.api;

import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.GroupDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.dto.requestDto.CreateGroupRequestDto;
import gachicar.gachicarserver.dto.requestDto.DeleteGroupRequestDto;
import gachicar.gachicarserver.dto.requestDto.UpdateGroupDescRequestDto;
import gachicar.gachicarserver.dto.requestDto.UpdateGroupNameRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.GroupService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 그룹(가족) 관련 Api
 * - 그룹 생성/조회/수정/삭제
 */
@Slf4j
@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupApiController {

    public final UserService userService;
    public final GroupService groupService;

    /**
     * 그룹 생성
     */
    @PostMapping
    public ResultDto<Object> createGroup(@RequestBody CreateGroupRequestDto requestDto) {
        try {
            User user = userService.findUserById(1L);
            Group group = groupService.createGroup(requestDto, user);
            userService.updateGroup(user, group);

            return ResultDto.of(HttpStatusCode.CREATED, "그룹 생성 성공", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 그룹 정보 조회
     */
    @GetMapping
    public ResultDto<GroupDto> getGroupInfo() {
        try {
            User user = userService.findUserById(1L);
            GroupDto groupDto = groupService.getUserGroup(user);

            return ResultDto.of(HttpStatusCode.OK, "그룹 정보 조회 성공", groupDto);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 그룹 닉네임 수정
     */
    @PatchMapping("/updateName")
    public ResultDto<Object> updateGroupName(@RequestBody UpdateGroupNameRequestDto requestDto) {
        try {
            User user = userService.findUserById(1L);
            groupService.updateGroupName(user, requestDto);

            return ResultDto.of(HttpStatusCode.OK, "그룹 닉네임 수정 성공", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 그룹 한줄 소개 수정
     */
    @PatchMapping("/updateDesc")
    public ResultDto<Object> updateGroupDesc(@RequestBody UpdateGroupDescRequestDto requestDto) {
        try {
            User user = userService.findUserById(1L);
            groupService.updateGroupDesc(user, requestDto);

            return ResultDto.of(HttpStatusCode.OK, "그룹 한줄소개 수정 성공", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 그룹 삭제
     */
    @DeleteMapping
    public ResultDto<Object> deleteGroup(@RequestBody DeleteGroupRequestDto requestDto) {
        try {
            User user = userService.findUserById(1L);
            userService.deleteGroup(user, requestDto);
            groupService.deleteGroup(requestDto);

            return ResultDto.of(HttpStatusCode.OK, "그룹 삭제 성공", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
