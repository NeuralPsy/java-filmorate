package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FIlmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class FilmoRateApplicationTests {

	@Autowired
	private final UserDbStorage userStorage;

	@Autowired
	private final FIlmDbStorage filmStorage;

	@Test
	@Sql(scripts = "/testdata.sql")
	public void shouldGetUserById() {
		User user = userStorage.getUserById(Long.valueOf(1));
		assertThat(user).hasFieldOrPropertyWithValue("id", Long.valueOf(1));
		assertThat(user).hasFieldOrPropertyWithValue("name", "user1");
		assertThat(user).hasFieldOrPropertyWithValue("login", "user1_login");
		assertThat(user).hasFieldOrPropertyWithValue("email", "user1@mail.ru");
		assertThat(user).hasFieldOrPropertyWithValue("birthday", "1990-03-15");
	}

	@Test
	public void shouldGetAllUsers(){
		List<User> users = userStorage.getAllUsers().stream().collect(Collectors.toList());
		assertThat(users.size()).isEqualTo(5);

		assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", Long.valueOf(1));
		assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "user1");
		assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "user1_login");
		assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "user1@mail.ru");
		assertThat(users.get(0)).hasFieldOrPropertyWithValue("birthday", "1990-03-15");

		assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", Long.valueOf(2));
		assertThat(users.get(1)).hasFieldOrPropertyWithValue("name", "user2_updated");
		assertThat(users.get(1)).hasFieldOrPropertyWithValue("login", "new_login");
		assertThat(users.get(1)).hasFieldOrPropertyWithValue("email", "new@gmail.com");
		assertThat(users.get(1)).hasFieldOrPropertyWithValue("birthday", "1987-04-11");

		assertThat(users.get(2)).hasFieldOrPropertyWithValue("id", Long.valueOf(3));
		assertThat(users.get(2)).hasFieldOrPropertyWithValue("name", "friend1");
		assertThat(users.get(2)).hasFieldOrPropertyWithValue("login", "friend1_login");
		assertThat(users.get(2)).hasFieldOrPropertyWithValue("email", "friend1@gmail.com");
		assertThat(users.get(2)).hasFieldOrPropertyWithValue("birthday", "1992-06-06");

		assertThat(users.get(3)).hasFieldOrPropertyWithValue("id", Long.valueOf(4));
		assertThat(users.get(3)).hasFieldOrPropertyWithValue("name", "friend2");
		assertThat(users.get(3)).hasFieldOrPropertyWithValue("login", "friend2_login");
		assertThat(users.get(3)).hasFieldOrPropertyWithValue("email", "friend2@gmail.com");
		assertThat(users.get(3)).hasFieldOrPropertyWithValue("birthday", "1991-09-21");
	}

	@Test
	public void shouldAddUser(){
		User user = User.builder()
				.name("new_user")
				.login("new_login")
				.email("new@email.com")
				.birthday("1987-09-10")
				.build();

		userStorage.addUser(user);

		User uploadedUser = userStorage.getUserById(Long.valueOf(5));

		assertThat(uploadedUser).hasFieldOrPropertyWithValue("id", Long.valueOf(5));
		assertThat(uploadedUser).hasFieldOrPropertyWithValue("name", "new_user");
		assertThat(uploadedUser).hasFieldOrPropertyWithValue("login", "new_login");
		assertThat(uploadedUser).hasFieldOrPropertyWithValue("birthday", "1987-09-10");
	}

	@Test
	public void shouldUpdateUser(){
		User user = User.builder()
				.id(Long.valueOf(2))
				.name("user2_updated")
				.login("new_login")
				.email("new@gmail.com")
				.birthday("1987-04-11")
				.build();
		userStorage.updateUser(user);

		User userUpdated = userStorage.getUserById(Long.valueOf(2));

		assertThat(userUpdated).hasFieldOrPropertyWithValue("id", Long.valueOf(2));
		assertThat(userUpdated).hasFieldOrPropertyWithValue("name", "user2_updated");
		assertThat(userUpdated).hasFieldOrPropertyWithValue("login", "new_login");
		assertThat(userUpdated).hasFieldOrPropertyWithValue("birthday", "1987-04-11");
	}

	@Test
	public void shouldAddFriend(){
		userStorage.addFriend(Long.valueOf(1), Long.valueOf(3));
		List<User> friends = userStorage.getFriendList(Long.valueOf(1)).stream().collect(Collectors.toList());

		assertThat(friends.size()).isEqualTo(1);
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("id", Long.valueOf(3));
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("name", "friend1");
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("login", "friend1_login");
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("birthday", "1992-06-06");

	}

	@Test
	public void shouldRemoveFriend(){
		List<User> friendsBefore = userStorage.getFriendList(Long.valueOf(2)).stream().collect(Collectors.toList());

		assertThat(friendsBefore.size()).isEqualTo(2);

		userStorage.removeFriend(Long.valueOf(2), Long.valueOf(4));

		List<User> friendsAfter = userStorage.getFriendList(Long.valueOf(2)).stream().collect(Collectors.toList());

		assertThat(friendsAfter.size()).isEqualTo(1);
	}

	@Test
	public void shouldGetCommonFriends(){
		List<User> friends = userStorage.getCommonFriends(Long.valueOf(2), Long.valueOf(3))
				.stream().collect(Collectors.toList());
		assertThat(friends.size()).isEqualTo(1);
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("id", Long.valueOf(1));
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("name", "user1");
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("login", "user1_login");
		assertThat(friends.get(0)).hasFieldOrPropertyWithValue("birthday", "1990-03-15");
	}

	@Test
	void shouldGetFilmById(){

	}

	@Test
//	@Sql(scripts = "src/test/resources/testdata2.sql")
	void shouldGetAllFilms(){
		List<Film> films = filmStorage.findAll().stream().collect(Collectors.toList());
		assertThat(films.size()).isEqualTo(2);

	}

	@Test
	public void shouldCreateFilm(){

	}

	@Test
	public void shouldUpdateFilm(){

	}

	@Test
	public void shouldRemoveFilm(){

	}

	@Test
	public void shouldLikeFilm(){

	}

	@Test
	public void shouldUnlikeFilm(){

	}

	@Test
	public void shouldGetTopFilms(){

	}

}
