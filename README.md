# FilmRate
Web-Service for movies and TV-shows access

## Database diagram
<img src="FIlmoRateDBD-2.drawio.png" alt="drawing" width="80%"/>

### SQL requests examples
**Get all films**

    SELECT *
    FROM films;
    
**Get all users**

    SELECT *
    FROM users;
    
**Get top 10 films**

    SELECT name
    FROM films
    ORDER BY likes_cout DESC
    LIMIT 10;

**Get friend list**

    SELECT friend_id
    FROM friendlist
    WHERE user_id = 0000    // User's ID should be instead of 0000
          AND status = 1;
    
