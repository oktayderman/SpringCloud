package org.barons.dto;

import lombok.*;


@Getter
@Setter
public class BookDTO {
    private Long id;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
