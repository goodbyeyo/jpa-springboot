package jpa.shop.domain.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M")
@Getter
//@NoArgsConstructor
public class Movie extends Item {

    private String director;
    private String actor;

    /*@Builder
    public Movie(String director, String actor) {
        this.director = director;
        this.actor = actor;
    }*/
}


