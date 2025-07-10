# LifePortfolioApp
Log your activities and time spent; see your life statistics.

Powered by JDK 17, Spring 3.5.0 + Spring Shell, SQLite (Hibernate).

This application is inspired by the [Strategic Life Portfolio](https://cameronlai.com/strategic-life-portfolio/) web-site and [this](https://habr.com/ru/companies/ru_mts/articles/912766/) habr article (in Russian) about
life management through logging daily activities and gathering their statistics.

Unlike simple diary-keeping, this approach:
- Has a predefined (yet simple, therefore flexible) structure;
- Is based upon logging, not plain text (therefore allows filtering, searching);
- Is meant not to get rid of a brain overload (_people usually keep diaries to be able to not remember everything in their life_), but specifically to see the statistics of your life.

![изображение](https://github.com/user-attachments/assets/232a3f1c-8657-49e0-826e-eed2c08f60d1)
_The application currently has only a CLI; yet this CLI is really easy to use and has everything one may require._

## Setting up
- Download the latest build;
- Change `.env` values (username, password, database name) if you want; they don't really matter, the app uses SQLite (no one will be able to connect to it from the web);
- Either double click the `.bat` file (Windows) or run the `.jar` archive itself (_search for how you run a Java program on your specific OS_). The app is (sadly) designed for Windows at the moment, so running on UNIX may require your personal involvement.

## How to
- Add some entry with the `log` command. It accepts a plain text line (no quotation marks required);
  - Don't forget to split your entry into **a group** and **a specific activity** with a `:` symbol (like: "Programming: Make some apps" or "Programming: Read some literature");
- Set the activity area and the PERMAV element with the related commands (type `help`);
- Set the time (in hours) with the specific command;
- Set the date, if required;
- Set the satisfaction / importance values.

 Every editor command (except `log`) takes an ID of an Entry as a first argument.

 You may use existing Entries as a template via `rpt` (means: repeat) command. Only the Entry group will be the same; all other info may be changed, if required.

 You may find all Entries from a group (it is enough to specify the first characters in the group name). It may be slow.

 You may filter Entries by their values. It is also possible to find all Entries from a group, but it's faster (because it searches inside a specific time period).

 Both `list` (weekly log) and `filter` commands alone are already good to see your stats; but if you use the `gen-report` command, you will receive a `.csv` file, which may be uploaded to the [Strategic Life Portfolio](https://cameronlai.com/strategic-life-portfolio/) web-site from earlier. There you will see a visual graph.

 ## PERMAV
Description by Gemini:
```
The term "permav elements" refers to the five core elements of
wellbeing and happiness as defined by the PERMA model,
plus an additional element, vitality, to create PERMA+V.
The acronym PERMA stands for Positive emotion, Engagement,
Relationships, Meaning, and Accomplishment. Adding "Vitality" (PERMA+V) emphasizes
the importance of physical health and well-being.
```
App counts: how often do you invest into each element of your well being? To see the count for elements by activities, type `permav`. To see a more detailed statistics, `filter` be a specific element (only first characters of the element required for filtering).

## Tips
It is okay if your activities from the same group (or even the same activity from different days) vary in importance / satisfation. The app will find the average.

It is okay if Activity Areas and PERMAV in your log don't perfectly align; make some combinations for each unit.

If you specify different Areas for a single group, the last one will override all the previous.

Use `help` to see all commands and their description; use the `-h` argument to see help for a specific command (like: `log -h` or `set -h`).

Use **absolute (not related to anything) values** for importance and satisfaction. One must not depend on other.

Sometimes you need to use `named parameters`; for example, `set 1 -s 4 -i 7` will set the _importance_ value to `7` and the _satisfaction_ value to `4` for the Entry with the ID of `1` (the first positional argument).
As usual, use `-h` with commands to see parameter (argument) names; and listing commands will provide you with the IDs for each Entry.

## Strategic Life Portfolio: specifics
_Activity Area_ is some specific area of your existense (e.g. Relations, Career, etc.); it may include numerous _Units_.

_Activity Unit_ is a specific activity that you engage in (e.g. Reading (a specific book or "just reading" - does not matter, it is your choice), Talking IRL / Chatting, Watching something, Walking, Drawing, Programming, Gaming, etc.); log every activity that has an impact on your life.

_Importance_ represents: does this specific Unit has a large impact on you? May be you can (or can't) live without it?

_Satisfaction_ is simply "Do I spend too much (or too little) time on this Unit? Am I satisfied with it right now?"

Basic approach: Increase time for **important** activities you are not satisfied with; spend less time on **unimportant** activities if you think they consume a lot of time.
