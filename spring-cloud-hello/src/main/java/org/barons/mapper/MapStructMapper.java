package org.barons.mapper;

import org.barons.Book;
import org.barons.dto.BookDTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface MapStructMapper {
    BookDTO bookToBookSlimDto(Book book);
    Book bookDTOtoBook(BookDTO dto);
}