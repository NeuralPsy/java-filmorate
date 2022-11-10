package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingDaoImpl;
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

	@Autowired
	private final MpaRatingDaoImpl mpaRatingDao;

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

		Film film = filmStorage.getById(Long.valueOf(1));

		assertThat(film).hasFieldOrPropertyWithValue("id", Long.valueOf(1));
		assertThat(film).hasFieldOrPropertyWithValue("name", "film1");
		assertThat(film).hasFieldOrPropertyWithValue("releaseDate", "1989-08-08");
		assertThat(film).hasFieldOrPropertyWithValue("description", "description of the film");
		assertThat(film).hasFieldOrPropertyWithValue("duration", Long.valueOf(139));
		assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1);



	}

	@Test
//	@Sql(scripts = "src/test/resources/testdata2.sql")
	void shouldGetAllFilms(){
		List<Film> films = filmStorage.findAll().stream().collect(Collectors.toList());
		assertThat(films.size()).isEqualTo(2);

		assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", Long.valueOf(1));
		assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "film1");
		assertThat(films.get(0)).hasFieldOrPropertyWithValue("releaseDate", "1989-08-08");
		assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "description of the film");
		assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", Long.valueOf(139));
		assertThat(films.get(0).getMpa()).hasFieldOrPropertyWithValue("id", 1);


		assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", Long.valueOf(3));
		assertThat(films.get(1)).hasFieldOrPropertyWithValue("name", "new_film");
		assertThat(films.get(1)).hasFieldOrPropertyWithValue("releaseDate", "2000-10-10");
		assertThat(films.get(1)).hasFieldOrPropertyWithValue("description", "text");
		assertThat(films.get(1)).hasFieldOrPropertyWithValue("duration", Long.valueOf(222));
		assertThat(films.get(1).getMpa()).hasFieldOrPropertyWithValue("id", 5);

	}

	@Test
	public void shouldCreateFilm(){
		Film film = Film.builder()
				.name("new_film")
				.releaseDate("2000-10-10")
				.description("text")
				.duration(Long.valueOf(222))
				.mpa(mpaRatingDao.getMpa(5))
				.build();

		filmStorage.addFilm(film);

		Film film_uploaded = filmStorage.getById(Long.valueOf(3));

				assertThat(film_uploaded).hasFieldOrPropertyWithValue("id", Long.valueOf(3));
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("name", "new_film");
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("releaseDate", "2000-10-10");
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("description", "text");
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("duration", Long.valueOf(222));
		assertThat(film_uploaded.getMpa()).hasFieldOrPropertyWithValue("id", 5);



	}

	@Test
	public void shouldUpdateFilm(){
		Film film = Film.builder()
				.id(Long.valueOf(2))
				.name("new_film_updated")
				.releaseDate("2001-10-10")
				.description("txt")
				.duration(Long.valueOf(212))
				.mpa(mpaRatingDao.getMpa(1))
				.build();

		filmStorage.update(film);

		Film film_uploaded = filmStorage.getById(Long.valueOf(2));

		assertThat(film_uploaded).hasFieldOrPropertyWithValue("id", Long.valueOf(2));
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("name", "new_film_updated");
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("releaseDate", "2001-10-10");
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("description", "txt");
		assertThat(film_uploaded).hasFieldOrPropertyWithValue("duration", Long.valueOf(212));
		assertThat(film_uploaded.getMpa()).hasFieldOrPropertyWithValue("id", 1);


	}

	@Test
	public void shouldRemoveFilm(){
		int count0 = filmStorage.findAll().size();
		assertThat(count0).isEqualTo(2);

		filmStorage.remove(Long.valueOf(2));

		int count = filmStorage.findAll().size();

		assertThat(count).isEqualTo(1);


	}



	@Test
	public void shouldUnlikeFilm(){
		Long preUnlikeCount = filmStorage.getLikesCount(Long.valueOf(1));
		assertThat(preUnlikeCount).isEqualTo(Long.valueOf(3));
		filmStorage.unlikeFilm(Long.valueOf(1), Long.valueOf(2));
		Long unlikeCount = filmStorage.getLikesCount(Long.valueOf(1));
		assertThat(unlikeCount).isEqualTo(Long.valueOf(2));


	}

	@Test
	public void shouldLikeFilm(){
		Long prelikeCount = filmStorage.getLikesCount(Long.valueOf(1));

		assertThat(prelikeCount).isEqualTo(Long.valueOf(2));

		filmStorage.likeFilm(Long.valueOf(1), Long.valueOf(4));

		Long likeCount = filmStorage.getLikesCount(Long.valueOf(1));

		assertThat(likeCount).isEqualTo(Long.valueOf(3));


	}

	@Test
	public void shouldGetTopFilmsWithoutCountValue(){

		List<Film> topFilms = filmStorage.showTopFilms(Integer.valueOf(10)).stream().collect(Collectors.toList());

		assertThat(topFilms.size()).isEqualTo(2);

		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("id", Long.valueOf(2));
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("name", "film2");
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("releaseDate", "1949-04-17");
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("description", "description of the film2");
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("duration", Long.valueOf(170));
		assertThat(topFilms.get(0).getMpa()).hasFieldOrPropertyWithValue("id", 3);
		assertThat(topFilms.get(0).getLikesCount()).isEqualTo(1);


		assertThat(topFilms.get(1)).hasFieldOrPropertyWithValue("id", Long.valueOf(1));
		assertThat(topFilms.get(1)).hasFieldOrPropertyWithValue("name", "film1");
		assertThat(topFilms.get(1)).hasFieldOrPropertyWithValue("releaseDate", "1989-08-08");
		assertThat(topFilms.get(1)).hasFieldOrPropertyWithValue("description", "description of the film");
		assertThat(topFilms.get(1)).hasFieldOrPropertyWithValue("duration", Long.valueOf(139));
		assertThat(topFilms.get(1).getMpa()).hasFieldOrPropertyWithValue("id", 1);
		assertThat(topFilms.get(0).getLikesCount()).isEqualTo(1);

		// add likescountcheck

	}

	@Test
	public void shouldGetTopFilmsWithCountValue(){

		List<Film> topFilms = filmStorage.showTopFilms(Integer.valueOf(1)).stream().collect(Collectors.toList());

		assertThat(topFilms.size()).isEqualTo(1);

		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("id", Long.valueOf(1));
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("name", "film1");
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("releaseDate", "1989-08-08");
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("description", "description of the film");
		assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("duration", Long.valueOf(139));
		assertThat(topFilms.get(0).getMpa()).hasFieldOrPropertyWithValue("id", 1);

	}

}
