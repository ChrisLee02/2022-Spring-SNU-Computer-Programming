import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class BackEnd extends ServerResourceAccessible {

    public boolean authenticate(String id, String pw) {
        try {
            String password;
            String dir = getServerStorageDir();
            File pwFile = new File(dir + id + "/password.txt");
            Scanner scanner = new Scanner(pwFile);
            password = scanner.nextLine();
            return password.equals(pw);
        } catch (Exception e) {
            return false;
        }
    }

    class PostWithDate extends Post implements Comparable {
        PostWithDate(String title, String content) {
            super(title, content);
        }

        PostWithDate(int id, LocalDateTime dateTime, String title, String content) {
            super(id, dateTime, title, content);
        }

        PostWithDate(int id, LocalDateTime dateTime, String advertising, String title, String content) {
            super(id, dateTime, advertising, title, content);
        }

        @Override
        public int compareTo(Object o) {
            PostWithDate other = (PostWithDate)o;
            return -1 * this.getDate().compareTo(other.getDate());
        }
    }

    public void post(String id, List titleContentList) {
        try {
            String title = (String) titleContentList.get(0);
            String advertising = (String) titleContentList.get(1);;
            String content = (String) titleContentList.get(2);
            content = content.substring(0, content.length()-2);
            String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            String entireContent = nowDateTime + "\n" + title + "\n" + advertising + "\n\n" + content;
            String dir = getServerStorageDir();
            List<String> userIDs = getEveryUser();
            int max = -1;
            for(String userid: userIDs) {
                File path = new File(dir + userid + "/post");
                File[] fileList = path.listFiles();
                for (File file : fileList) {
                    int fileNumber = getFileID(file);
                    if (fileNumber > max) max = fileNumber;
                }
            }
            FileWriter fileWriter = new FileWriter(dir + id + "/post/" + (max + 1) + ".txt");
            fileWriter.write(entireContent);
            fileWriter.close();
        } catch (Exception e) {
            return;
        }
    }

    class PostWithKeywords extends Post implements Comparable {
        public int totalKeywords;
        public int totalWordCount;

        PostWithKeywords(String title, String content) {
            super(title, content);
            totalKeywords = 0;
            totalWordCount = 0;
        }

        PostWithKeywords(int id, LocalDateTime dateTime, String title, String content) {
            super(id, dateTime, title, content);
            totalKeywords = 0;
            totalWordCount = 0;
        }

        PostWithKeywords(int id, LocalDateTime dateTime, String advertising, String title, String content) {
            super(id, dateTime, advertising, title, content);
            totalKeywords = 0;
            totalWordCount = 0;
        }

        public void countKeywords(Set<String> keywords) {
            try {
                if(totalKeywords != 0) {
                    return;
                }
                for(String keyword: keywords) {
                    for(String titleWord: getTitle().split(" ")) {
                        if(titleWord.equals(keyword)) totalKeywords++;
                    }

                    for(String contentLine: getContent().split("\n")) {
                        for(String contentWord: contentLine.split(" ")) {
                            totalWordCount++;
                            if(contentWord.equals(keyword)) totalKeywords++;
                        }
                    }
                }
            } catch (Exception e) {
                return;
            }
        }

        @Override
        public int compareTo(Object o) {

            PostWithKeywords other = (PostWithKeywords) o;

            if(this.totalKeywords == other.totalKeywords) {
                return -1*(this.getContentWordNum() - other.getContentWordNum());
            }
            else {
                return -1*(this.totalKeywords - other.totalKeywords);
            }

        }


    }

    int getFileID(File file) {
        return Integer.parseInt(file.getName().substring(0, file.getName().indexOf(".")));
    }

    void getPosts(List<PostWithDate> adPosts, List<PostWithDate> nonAdPosts, List<String> friends) {
        String dir = getServerStorageDir();
        for (String friendID : friends) {
            File path = new File(dir + friendID + "/post");
            File[] fileList = path.listFiles(); //0번째 줄과 2번째 줄이 각각 날짜/광고 여부
            for (File file : fileList) {
                try {
                    Scanner scanner = new Scanner(file);
                    List<String> contents = new LinkedList<>();
                    //여기 id추가, 날짜 앞에 추가,
                    int id = getFileID(file);
                    String dateTime = scanner.nextLine();
                    String title = scanner.nextLine();
                    String isAd = scanner.nextLine();
                    scanner.nextLine();
                    while(scanner.hasNext()) {
                        contents.add(scanner.nextLine());
                    }
                    String entireContent = String.join("\n", contents);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime date = LocalDateTime.parse(dateTime,formatter);
                    PostWithDate postWithDate = new PostWithDate(id, date, isAd, title, entireContent);
                    if(isAd.equals("yes")) {
                        adPosts.add(postWithDate);
                    }
                    else {
                        nonAdPosts.add(postWithDate);
                    }
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    return;
                }
            }
        }
    }

    void getPostsWithKeyword(List<PostWithKeywords> posts, List<String> friends, Set<String> keywords) {
        String dir = getServerStorageDir();
        for (String friendID : friends) {
            File path = new File(dir + friendID + "/post");
            File[] fileList = path.listFiles(); //0번째 줄과 2번째 줄이 각각 날짜/광고 여부
            for (File file : fileList) {
                try {
                    Scanner scanner = new Scanner(file);
                    List<String> contents = new LinkedList<>();
                    //여기 id 추가, 날짜 앞에 추가,
                    int id = getFileID(file);
                    String dateTime = scanner.nextLine();
                    String title = scanner.nextLine();
                    String isAd = scanner.nextLine();
                    scanner.nextLine();
                    while(scanner.hasNext()) {
                        contents.add(scanner.nextLine());
                    }
                    String entireContent = String.join("\n", contents);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime date = LocalDateTime.parse(dateTime,formatter);
                    PostWithKeywords postWithKeywords = new PostWithKeywords(id, date, isAd, title, entireContent);
                    postWithKeywords.countKeywords(keywords);
                    posts.add(postWithKeywords);
                    //System.out.println(postWithKeywords.getSummary()); // 주석처리
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    return;
                }

            }
        }
    }

    public String[] recommend(int N, String id) {
        String[] results = new String[N];
        List<String> friends = getFriends(id);
        List<PostWithDate> adPosts = new LinkedList<>();
        List<PostWithDate> nonAdPosts = new LinkedList<>();
        getPosts(adPosts, nonAdPosts, friends);
        Collections.sort(adPosts);
        Collections.sort(nonAdPosts);

        int i = 0;
        for(PostWithDate post: adPosts) {
            if(i>=N) return results;
            results[i] = post.toString();
            i++;
        }
        for(PostWithDate post: nonAdPosts) {
            if(i>=N) return results;
            results[i] = post.toString();
            i++;
        }

        return results;
    }

    public List<String> search(Set<String> keywords, String id) {

        List<String> results = new LinkedList<>();

        List<String> friends = getEveryUser();
        List<PostWithKeywords> posts = new LinkedList<>();
        getPostsWithKeyword(posts, friends, keywords);
        Collections.sort(posts);
        int i = 0;
       /* System.out.println(friends);
        System.out.println(posts.size());*/
        /*for(PostWithKeywords post: posts) {
            System.out.println(post.getSummary());
        }*/
        for(PostWithKeywords post: posts) {
            if(i>=10) return results;
            results.add(post.getSummary());
            i++;
        }

        return results;
    }

    List<String> getEveryUser() {
        List<String> friends = new LinkedList<>();
        String dir = getServerStorageDir();
        File rootDir = new File(dir);
        for(File file: rootDir.listFiles()) {
            friends.add(file.getName());
        }


        return friends;
    }

    List<String> getFriends(String id) {
        List<String> friends = new LinkedList<>();
        String dir = getServerStorageDir();
        File friendsFile = new File(dir + id + "/friend.txt");
        try {
            Scanner scanner = new Scanner(friendsFile);
            while (scanner.hasNext()) {
                String friend = scanner.nextLine();
                friends.add(friend);
            }
        } catch (Exception e) {
            return null;
        }
        return friends;
    }
    // Use getServerStorageDir() as a default directory
    // TODO sub-program 1 ~ 4 :
    // Create helper functions to support FrontEnd class

}
