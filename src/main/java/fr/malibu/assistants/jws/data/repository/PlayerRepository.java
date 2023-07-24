package fr.malibu.assistants.jws.data.repository;


import javax.enterprise.context.ApplicationScoped;


import fr.malibu.assistants.jws.domain.entity.PlayerEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<PlayerEntity> {


}
