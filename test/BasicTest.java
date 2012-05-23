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
}
