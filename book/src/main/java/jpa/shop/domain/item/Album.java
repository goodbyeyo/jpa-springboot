package jpa.shop.domain.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
//@NoArgsConstructor
public class Album extends Item {

    private String artist;
    private String etc;


    /*@Builder
    public Album(String artist, String etc) {
        this.artist = artist;
        this.etc = etc;
    }*/
}
