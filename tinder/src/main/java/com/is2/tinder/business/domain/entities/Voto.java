package com.is2.tinder.business.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Voto {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @ManyToOne
    private Mascota mascota1; //mascota que genera el voto
    @ManyToOne
    private Mascota mascota2; //mascota que recibe el voto
    private boolean eliminado = false;
    private Date respuesta;

}
