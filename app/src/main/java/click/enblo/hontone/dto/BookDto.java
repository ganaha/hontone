package click.enblo.hontone.dto;

import java.util.List;

/**
 * 楽天ブックス書籍検索APIのレスポンスDTO(Book)
 */
public class BookDto {

    public int count;
    public int page;
    public int first;
    public int last;
    public int hits;
    public int carrier;
    public int pageCount;
    public List<ItemsDto> Items;
}
