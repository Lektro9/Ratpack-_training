import ratpack.exec.Promise;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import ratpack.util.MultiValueMap;

import java.util.ArrayList;

import static ratpack.jackson.Jackson.json;

public class App {
    public static void main(String[] args) throws Exception {

        User user1 = new User(1, "Lars", "lkroll@scoop-software.de");
        User user2 = new User(2, "Tobi", "tobi@scoop-software.de");
        User user3 = new User(3, "Daniel", "Daniel@gmail.com");
        User user4 = new User(4, "Isa", "Isa@scoop-software.de");
        User user5 = new User(5, "Ronnie", "Ronnie@gmail.com");
        User user6 = new User(6, "James", "james@web.de");
        User user7 = new User(7, "Robert", "bob@scoop-software.de");
        User user8 = new User(8, "Denise", "denise@gmail.com");
        User user9 = new User(9, "Andreas", "Andreas@scoop-software.de");
        User user10 = new User(10, "Claire", "Claire@scoop-software.de");

        ArrayList<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
        users.add(user8);
        users.add(user9);
        users.add(user10);


        RatpackServer.start(serverSpec -> serverSpec
                .serverConfig(c -> c.baseDir(BaseDir.find()))
                .handlers(chain -> chain
                        .prefix("home",
                                path -> path.fileSystem("static", Chain::files))
                        .get(ctx -> {
                            MultiValueMap<String, String> nameMap = ctx.getRequest().getQueryParams();
                            String name = nameMap.get("name");
                            ctx.render("Hello " + name);
                        })
                        .path("foo", ctx ->
                                ctx.byMethod(s -> s
                                        .get(() -> ctx.render("goo"))
                                        .post(() -> {
                                            ctx.render("success, but dont know how to grab content, yet");
                                        })
                                ))
                        .get("users", ctx ->
                                ctx.byContent(m -> m
                                        .html(() -> {
                                            String content = "";
                                            for (int i = 0; i < users.size(); i++) {
                                                content += "<p>" + users.get(i).name + "</p>";
                                                content += "<p>" + users.get(i).email + "</p>";
                                            }
                                            ctx.render(content);
                                        })
                                        .json(() -> ctx.render(json(users))
                                        )
                                )
                        )
                        .post("users/new", ctx -> {
                            Promise<User> user = ctx.parse(Jackson.fromJson(User.class));
                            user.then(u -> {
                                users.add(u);
                                ctx.render(u.name);
                            });
                        })
                        .put("users/update", ctx -> {
                            Promise<User> user = ctx.parse(Jackson.fromJson(User.class));
                            user.then(u -> {
                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).id == u.id) {
                                        users.get(i).name = u.name;
                                        users.get(i).email = u.email;
                                    }
                                }
                                ctx.render("tried to update");
                            });
                        })
                )
        );
    }
}