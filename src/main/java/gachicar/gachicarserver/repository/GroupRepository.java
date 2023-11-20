package gachicar.gachicarserver.repository;

import gachicar.gachicarserver.domain.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Group group) {
        em.persist(group);
    }

    public Group findById(Long groupId) {
        return em.find(Group.class, groupId);
    }
}
