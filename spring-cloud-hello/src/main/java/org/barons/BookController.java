package org.barons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.barons.dto.BookDTO;
import org.barons.mapper.MapStructMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private MapStructMapper mapstructMapper;
    @Autowired
    BookService bookService;


    public List<Book> books() {
        List<Book> list = bookService.list();
        list.get(0).setId((long) Integer.MAX_VALUE);
        return list;
    }

    @PostMapping
    public Book create(@RequestBody BookDTO dto) {
        return bookService.save(mapstructMapper.bookDTOtoBook(dto));
    }

    public static void main(String[] args) throws JsonProcessingException {
        Book book = new Book();
        book.setName("test");
        System.out.println(new ObjectMapper().writeValueAsString(book));
    }

}
