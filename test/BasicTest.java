import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

  @Before
  public void setup() {
    Fixtures.deleteDatabase();
  }

  @Test
  public void createAndRetrieveUser() {
    new User("bob@gmail.com", "secret", "Bob").save();

    User bob = User.find("byEmail", "bob@gmail.com").first();

    assertNotNull(bob);
    assertEquals("Bob", bob.fullname);
  }

  @Test
  public void tryConnectAsUser() {
    new User("bob@gmail.com", "secret", "Bob").save();

    assertNotNull(User.connect("bob@gmail.com", "secret"));
    assertNull(User.connect("bob@gmail.com", "badpassword"));
    assertNull(User.connect("tom@gmail.com", "secret"));
  }

  @Test
  public void createPost() {
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    new Post(bob, "Hello world").save();

    assertEquals(1, Post.count());

    List<Post> bobPosts = Post.find("byAuthor", bob).fetch();

    assertEquals(1, bobPosts.size());
    Post firstPost = bobPosts.get(0);
    assertNotNull(firstPost);
    assertEquals(bob, firstPost.author);
    assertEquals("Hello world", firstPost.content);
    assertNotNull(firstPost.createdAt);
  }

  @Test
  public void postComments() {
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    Post bobPost = new Post(bob, "Hello world").save();

    new Comment(bobPost, bob, "Nice post").save();
    new Comment(bobPost, bob, "I knew that !").save();

    List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();

    assertEquals(2, bobPostComments.size());

    Comment firstComment = bobPostComments.get(0);
    assertNotNull(firstComment);
    assertEquals("Bob", firstComment.author.fullname);
    assertEquals("Nice post", firstComment.content);
    assertNotNull(firstComment.createdAt);

    Comment secondComment = bobPostComments.get(1);
    assertNotNull(secondComment);
    assertEquals("Bob", secondComment.author.fullname);
    assertEquals("I knew that !", secondComment.content);
    assertNotNull(secondComment.createdAt);
  }

  @Test
  public void useTheCommentsRelation() {
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    Post bobPost = new Post(bob, "Hello world").save();

    bobPost.addComment(bob, "Nice post");
    bobPost.addComment(bob, "I knew that !");

    assertEquals(1, User.count());
    assertEquals(1, Post.count());
    assertEquals(2, Comment.count());

    bobPost = Post.find("byAuthor", bob).first();
    assertNotNull(bobPost);

    assertEquals(2, bobPost.comments.size());
    assertEquals("Bob", bobPost.comments.get(0).author.fullname);

    bobPost.delete();

    assertEquals(1, User.count());
    assertEquals(0, Post.count());
    assertEquals(0, Comment.count());
  }

//  @Test
//  public void fullTest() {
//    Fixtures.loadModels("data.yml");
//
//    // Count things
//    assertEquals(2, User.count());
//    assertEquals(3, Post.count());
//    assertEquals(3, Comment.count());
//
//    // Try to connect as users
//    assertNotNull(User.connect("bob@gmail.com", "secret"));
//    assertNotNull(User.connect("jeff@gmail.com", "secret"));
//    assertNull(User.connect("jeff@gmail.com", "badpassword"));
//    assertNull(User.connect("tom@gmail.com", "secret"));
//
//    // Find all of Bob's posts
//    List<Post> bobPosts = Post.find("author.email", "bob@gmail.com").fetch();
//    assertEquals(2, bobPosts.size());
//
//    // Find all comments related to Bob's posts
//    List<Comment> bobComments = Comment.find("post.author.email", "bob@gmail.com").fetch();
//    assertEquals(3, bobComments.size());
//
//    // Fix the most recent post
//    Post frontPost = Post.find("order by postedAt desc").first();
//    assertNotNull(frontPost);
//
//    // Check that this post has two comments
//    assertEquals(2, frontPost.comments.size());
//    
//    // Post a new comment
//    //frontPost.addComment("Jim", "Hello guys");
//    User jim = User.connect("jeff@gmail.com", "secret");
//    frontPost.addComment(jim, "Hello guys");
//    assertEquals(3, frontPost.comments.size());
//    assertEquals(4, Comment.count());
//  }

}
