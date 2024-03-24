package gachicar.gachicarserver.repository;

import gachicar.gachicarserver.domain.GroupEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(GroupEntity group) {
        em.persist(group);
    }

    public GroupEntity findById(Long groupId) {
        return em.find(GroupEntity.class, groupId);
    }

    public GroupEntity findByName(String groupName) {
        try {
            return em.createQuery("SELECT g FROM GroupEntity g WHERE g.name = :groupName", GroupEntity.class)
                    .setParameter("groupName", groupName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // 결과가 없으면 null 반환
        }
    }

    public void delete(GroupEntity group) {
        em.remove(group);
    }
}
