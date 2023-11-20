package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.GroupDto;
import gachicar.gachicarserver.dto.requestDto.CreateGroupRequestDto;
import gachicar.gachicarserver.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    public final GroupRepository groupRepository;

    public Group createGroup(CreateGroupRequestDto requestDto, User user) {
        Group newGroup = Group.builder()
                .name(requestDto.getGroupName())
                .desc(requestDto.getGroupDesc())
                .manager(user)
                .build();
        groupRepository.save(newGroup);
        return newGroup;
    }
}
