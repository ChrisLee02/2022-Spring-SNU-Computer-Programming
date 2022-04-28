import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.Adler32;

class Post {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private int id;
    private final static int ID_NOT_INITIATED = -1;
    private LocalDateTime dateTime;
    private String title, content;
    private String advertising;

    Post(String title, String content) {
        this(ID_NOT_INITIATED, LocalDateTime.now(), title, content);
    }

    Post(int id, LocalDateTime dateTime, String title, String content) {
        this.id = id;
        this.dateTime = dateTime;
        this.title = title;
        this.content = content.trim();
    }

    Post(int id, LocalDateTime dateTime, String advertising, String title, String content) {
        this.id = id;
        this.dateTime = dateTime;
        this.advertising = advertising;
        this.title = title;
        this.content = content.trim();
    }

    String getSummary() {
        return String.format("id: %d, created at: %s, title: %s", id, getDate(), title);
    }

    @Override
    public String toString() {
        return String.format(
            "-----------------------------------\n" +
            "id: %d\n" +
            "created at: %s\n" +
            "title: %s\n" +
            "content: %s"
            , id, getDate(), title, content);
}

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getDate() {
        return dateTime.format(formatter);
    }

    void setDateTime(LocalDateTime dateTime){
        this.dateTime = dateTime;
    }

    String getTitle() {
        return title;
    }
    String getAdvertising() { return advertising;    }

    String getContent() {
        return content;
    }

    int getContentWordNum(){
        return (int) content.replaceAll("\n"," ").split(" ").length;
    }

    static LocalDateTime parseDateTimeString(String dateString, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(dateString,dateTimeFormatter);
    }
}
