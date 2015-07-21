package click.enblo.hontone.dto;

/**
 * 楽天ブックス書籍検索APIのレスポンスDTO(Item)
 */
public class ItemDto {

    /**
     * API
     */
    // 書籍タイトル
    public String title;
    // 書籍タイトル カナ
    public String titleKana;
    // 書籍サブタイトル
    public String subTitle;
    // 書籍サブタイトル カナ
    public String subTitleKana;
    // 叢書名
    public String seriesName;
    // 叢書名カナ
    public String seriesNameKana;
    // 多巻物収録内容
    public String contents;
    // 著者名
    public String author;
    // 著者名カナ
    public String authorKana;
    // 出版社名
    public String publisherName;
    // 書籍のサイズ
    public String size;
    // ISBNコード(書籍コード)
    public String isbn;
    // 商品説明文
    public String itemCaption;
    // 発売日
    public String salesDate;
    // 税込み販売価格
    public int itemPrice;
    // 定価
    public int listPrice;
    // 割引率
    public int discountRate;
    // 割引額
    public int discountPrice;
    // 商品URL
    public String itemUrl;
    // アフィリエイトURL
    public String affiliateUrl;
    // 商品画像 64x64URL
    public String smallImageUrl;
    // 商品画像 128x128URL
    public String mediumImageUrl;
    // 商品画像 200x200URL
    public String largeImageUrl;
    // チラよみURL
    public String chirayomiUrl;
    // 在庫状況
    public String availability;
    // 送料フラグ
    public int postageFlag;
    // 限定フラグ
    public int limitedFlag;
    // レビュー件数
    public int reviewCount;
    // レビュー平均
    public String reviewAverage;
    // ジャンルID
    public String booksGenreId;

    /**
     * 独自
     */
    // 登録日時
    public String registered;
    // kintone ID(アップロード済みであることを示す)
    public String kintoneId;
}
