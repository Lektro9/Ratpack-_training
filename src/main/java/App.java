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

        ArrayList<User> users = new ArrayList<User>();
        users.add(user2);
        users.add(user1);

        RatpackServer.start(serverSpec -> serverSpec
                .serverConfig(c -> c.baseDir(BaseDir.find()).build())
                .handlers(chain -> chain
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
                )
        );
    }
}