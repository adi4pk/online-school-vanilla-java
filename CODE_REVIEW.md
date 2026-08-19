# Code review — online-school-vanilla-java

**Commit revizuit:** `a92b793` (Book, BookRepository, Course, CourseRepository, CardStudent, Student)
**Data:** 2026-08-19
**Scop:** intrarea în relații între entități (Student ↔ Book, Student ↔ CardStudent, Course) peste stratul de repository existent.

Codul **compilează curat** (`javac -Xlint:all` → doar 4 warning-uri `this-escape`, benigne aici).
Toate constatările de mai jos au fost **reproduse prin rulare**, pe o copie a repo-ului — fișierele tale de date nu au fost atinse.

---

## Ce e bine

Tiparul de repository e acum internalizat: `findById` / `findByX` cu `Optional`, `existsByX` delegat la `findByX().isPresent()`, `indexOf` care returnează `-1`, `save()` care rescrie tot fișierul, `loadData()` cu număr de linie în mesajul de eroare. L-ai aplicat de trei ori, singur, fără model în față. Asta e exact ce trebuia să iasă din runda trecută.

Comentariile de studiu din cod (ce returnează fiecare metodă, de ce `continue`, ce face `scanner`) sunt un semn bun — arată că ai reconstruit mecanismul, nu l-ai copiat. Le poți curăța la final, dar acum își fac treaba.

---

## 🔴 Critice

### B1 — `java app.Main` crapă la orice rulare
`src/app/Main.java:12-14`

```
Exception in thread "main" java.lang.IllegalArgumentException: Email already used: gabriela2.rusu@example.com
	at app.student.repository.StudentRepository.add(StudentRepository.java:56)
	at app.Main.main(Main.java:14)
```

`Main` adaugă necondiționat același student la fiecare pornire. Prima rulare a mers și a scris linia 11 în `students.txt`; de atunci, orice rulare se oprește în excepție înainte să apuce să facă altceva.

**Restanță din review-ul anterior — încă deschisă.**

Nu e o eroare de logică în repository (validarea e corectă), e o eroare de *scenariu*: `Main` nu e idempotent. Un `main` de probă trebuie să poată fi rulat de o sută de ori la rând cu același rezultat.

---

### B2 — `update()` acceptă emailuri duplicate și le salvează pe disc
`src/app/student/repository/StudentRepository.java:69-74` și, prin copy-paste, `src/app/book/repository/BookRepository.java:85-90`

Reprodus: iau studentul de pe poziția 0, îi pun emailul studentului de pe poziția 1, chem `update()`. Rezultat:

```
>>> UPDATE A TRECUT (ar fi trebuit respins)
   a5e-12-32 -> andrei.ionescu@example.com
   c8f-45-71 -> andrei.ionescu@example.com
```

Două conturi cu același email, **scrise în fișier**. Regula de unicitate a fost ocolită complet.

**Mecanismul.** `findAll()` returnează o copie a *listei*, dar nu a *obiectelor* — sunt aceleași referințe. Deci când faci `s.setEmail(...)`, ai modificat deja obiectul **dinăuntrul** repository-ului, înainte ca `update()` să fi validat ceva. Când `update()` cheamă apoi `findByEmail(student.getEmail())`, prima potrivire găsită în listă este chiar obiectul pe care tocmai l-ai modificat. Verificarea devine „am eu același id cu mine însumi?" → `true` → nicio excepție.

Validarea nu are cum să apere o stare care s-a schimbat deja sub ea.

**Precizare de onestitate:** `StudentRepository.update()` e codul pe care l-am scris eu în runda trecută (`7d6ecd1`), deci bug-ul e al meu. Tu l-ai propagat corect în `BookRepository` — ai copiat un tipar greșit, nu ai inventat unul. Dar acum îl repari în ambele locuri, pentru că lecția e reală: **o metodă de validare care primește ca argument chiar obiectul din colecție nu poate valida nimic.**

---

### B3 — `CourseRepository` nu încarcă niciodată datele
`src/app/course/repository/CourseRepository.java:20-23`

```java
public CourseRepository(){


}
```

Constructorul e gol — `loadData()` nu e apelat. Reprodus: `new CourseRepository().countCourses()` → **0**, mereu, indiferent ce e în fișier. `findAllCourses()`, `findById()`, `findByCourseName()` returnează gol/`Optional.empty()` pentru totdeauna.

Consecință în lanț: `loadData()` și `save()` sunt `private` și nu le cheamă nimeni → cod mort. Iar clasa nu are `addCourse` / `updateCourse` / `deleteById`, deci nici nu există vreo cale prin care `courses.txt` (0 bytes acum) să fie vreodată populat.

Compară cu `StudentRepository:20` și `BookRepository:21` — acolo constructorul cheamă `loadData()`. Compilatorul nu te ajută aici: un constructor gol e legal. Doar rularea te prinde.

---

### B4 — `addBook()` aruncă id-ul primit și îl înlocuiește cu UUID
`src/app/book/repository/BookRepository.java:71`

Reprodus:
```
C) id inainte de addBook = zzz-00-00
   id dupa addBook      = 567f5fe1-7b8c-44b9-ab17-d36fc92916d6
```

`Book(String text)` parsează cu grijă `arr[0]` în `bookId`, iar `addBook()` îl suprascrie imediat. Rezultatul: `books.txt` devine mixt — 10 id-uri scurte încărcate din fișier plus UUID-uri pentru tot ce adaugi la runtime. Exact ce s-a întâmplat deja cu `students.txt`, unde linia 11 e `9755fd14-6373-4854-b98f-d8298379b95c` lângă zece `a5e-12-32`.

**Decizia de format de id e tot restanță din runda trecută și acum s-a multiplicat în trei clase.** Trebuie luată acum, o dată, pentru toate entitățile:

| Variantă | Ce implică |
|---|---|
| UUID peste tot | repository-ul generează id-ul; fișierele de test se rescriu cu UUID-uri; `Book("nume,data")` fără id în text |
| id din fișier | repository-ul **nu** generează nimic, doar validează unicitatea id-ului primit |

Ce nu merge e ce ai acum: constructorul citește un id, repository-ul îl ignoră.

---

### B5 — aceeași carte poate ajunge la doi studenți deodată
`src/app/student/model/Student.java:40-43` + `src/app/book/Book.java:23-25`

Reprodus: `s1.cumparaCarte(carte)` apoi `s2.cumparaCarte(carte)`. Cartea rămâne în `arrBooks` la **amândoi**, dar `carte.student` reține doar pe `s2`. Relația bidirecțională a devenit inconsistentă: dintr-o parte se vede una, din cealaltă alta.

`cumparaCarte` setează legătura, dar nu o **desface** pe cea veche. Ai definit `Book.student` ca „o carte poate avea doar un student" (comentariul tău de la `Book.java:13`), dar nimic din cod nu apără invariantul ăsta.

Aceeași problemă la `deleteByBookId` (`BookRepository:104`): scoți cartea din repository, dar referința rămâne în `Student.arrBooks` — student cu o carte care nu mai există.

---

## 🟡 Importante

- **M1 — relațiile nu se salvează deloc.** `Student.toText():99` scrie 6 câmpuri, fără cărți și fără card. `Book.toText():51` scrie 3 câmpuri, fără student. Deci toată munca de la B5 trăiește doar în RAM: la repornire, fiecare student are 0 cărți și fiecare carte 0 studenți. Trebuie decis unde ține fișierul legătura — cel mai simplu, `books.txt` primește o a 4-a coloană cu `studentId` (partea „many" ține cheia străină, exact ca în schema ta din `public/schema.jpeg`).

- **M2 — `setCard(null)` → `NullPointerException`.** `Student.java:35-38`: `card.setStudent(this)` fără verificare de null. Reprodus. În plus, `CardStudent` nu are `getStudent()` (nu poți verifica legătura din cealaltă parte), iar `CardStudent.setStudent()` e `public` — cine îl cheamă direct sparge legătura, pentru că nu setează și `student.card`. Într-o relație bidirecțională, **un singur capăt trebuie să fie „proprietarul" metodei publice**; celălalt devine package-private sau intern.

- **M3 — `Course.registeredStudents:14` e declarat și nu e populat niciodată.** Nu există `inscrieStudent()` / `retrageStudent()`. Câmpul e, deocamdată, decor. Iar `CourseRepository` n-are niciun fel de scriere (add/update/delete).

- **M4 — validarea de unicitate pe numele cărții.** `addBook():67` refuză a doua carte cu același titlu. Într-o școală vrei, probabil, două exemplare din „Clean Code". Dacă da, unicitatea trebuie mutată pe id, iar `existsByBookName` rămâne doar căutare. Decizie de modelare — spune tu care e intenția.

- **M5 — `Book` nu e în `app.book.model`.** `Student` e în `app.student.model`, `Course` în `app.course.model`, `Book` direct în `app.book`. Structura devine imprevizibilă când crește.

---

## 🟢 Cleanups

- **C1** — `Student.java:103-107`: câmpuri de test rămase în clasă (`int x`, `String text`, `Integer y`, `ArrayList<Integer> arr`). Sunt câmpuri de instanță reale: fiecare `Student` cară un `ArrayList` gol în plus.
- **C2** — `BookRepository.java:8-13`: `java.util.*` deja importă `Scanner`, `Optional`, `List`, `UUID`; următoarele 4 importuri sunt redundante.
- **C3** — `BookRepository` aruncă `IllegalArgumentException` la erori de I/O (liniile 137, 141, 154), unde `StudentRepository` și `CourseRepository` aruncă `IllegalStateException`. „Fișierul nu se poate citi" nu e o problemă de argument. Alege una și ține-o.
- **C4** — `Book.setCreated_at()` / `getCreated_at()`: snake_case într-un cod altfel camelCase. `setCreatedAt` / `getCreatedAt`.
- **C5** — `indexOfBook()` e `public` în `BookRepository`, `indexOf()` e `private` în `StudentRepository`. E un detaliu intern → `private`.
- **C6** — cod comentat de șters: `BookRepository:98-102` și `:105-109`, `Student.java:30-31`.
- **C7** — `books.txt` nu are newline final; `courses.txt` e gol (0 bytes).

---

## Before / After — doar pentru critice

### B1 — `Main` idempotent

| Acum (`Main.java:12-14`) | Cum ar trebui |
|---|---|
| <pre>Student student = new Student(<br>  "t8v-16-92,Gabriela,...");<br>studentRepository.add(student);</pre> | <pre>String email = "gabriela2.rusu@example.com";<br>if (!studentRepository.existsByEmail(email)) {<br>    studentRepository.add(new Student(...));<br>}<br>System.out.println(studentRepository.count());</pre> |

Ai deja `existsByEmail()` scris — nu l-ai folosit niciodată. Un `main` de probă ar trebui să *citească* și să afișeze, nu doar să scrie orbește.

### B2 — validare care nu se poate ocoli

| Acum (`StudentRepository.java:69-74`) | Cum ar trebui |
|---|---|
| <pre>Optional&lt;Student&gt; byEmail =<br>    findByEmail(student.getEmail());<br>if (byEmail.isPresent()<br>    && !byEmail.get().getId()<br>        .equals(student.getId())) {<br>    throw new IllegalArgumentException(...);<br>}<br>students.set(index, student);</pre> | <pre>for (Student other : students) {<br>    if (other == student) continue;<br>    if (other.getId().equals(student.getId())) continue;<br>    if (other.getEmail()<br>          .equalsIgnoreCase(student.getEmail())) {<br>        throw new IllegalArgumentException(<br>            "Email already used: " + student.getEmail());<br>    }<br>}<br>students.set(index, student);</pre> |

Diferența e `other == student`: sari peste obiectul însuși prin **identitate de referință**, nu prin id. Comparația pe id nu ajută, pentru că obiectul modificat *are* id-ul corect — el e chiar cel pe care ai voie să-l modifici. Aceeași corecție în `BookRepository.updateBook()` pentru `bookName`.

### B3 — constructorul care încarcă

| Acum (`CourseRepository.java:20-23`) | Cum ar trebui |
|---|---|
| <pre>public CourseRepository(){<br><br><br>}</pre> | <pre>public CourseRepository(){<br>    loadData();<br>}</pre> |

Plus `addCourse` / `updateCourse` / `deleteById` după tiparul din `BookRepository`, altfel `save()` rămâne cod mort.

### B4 — cine decide id-ul

| Acum (`BookRepository.java:71`) | Varianta „id din fișier" |
|---|---|
| <pre>book.setBookId(UUID.randomUUID().toString());<br>books.add(book);</pre> | <pre>if (findById(book.getBookId()).isPresent()) {<br>    throw new IllegalArgumentException(<br>        "Book id already used: " + book.getBookId());<br>}<br>books.add(book);</pre> |

Sau varianta „UUID peste tot" — dar atunci scoate id-ul din constructorul `Book(String text)` și rescrie `books.txt`. **Alege una și aplic-o în toate cele trei repository-uri.**

### B5 — o carte, un singur proprietar

| Acum (`Student.java:40-43`) | Cum ar trebui |
|---|---|
| <pre>public void cumparaCarte(Book book){<br>    arrBooks.add(book);<br>    book.assignToStudent(this);<br>}</pre> | <pre>public void cumparaCarte(Book book){<br>    if (arrBooks.contains(book)) return;<br>    Student owner = book.getStudent();<br>    if (owner != null) {<br>        owner.arrBooks.remove(book);<br>    }<br>    arrBooks.add(book);<br>    book.assignToStudent(this);<br>}</pre> |

Necesită un `getStudent()` în `Book`. Regula generală pentru relații bidirecționale: **înainte să legi, dezleagă.**

---

## Q&A — verificare de înțelegere

1. `findAll()` face `return new ArrayList<>(students)` — deci returnează o listă nouă. De ce, atunci, `s.setEmail(...)` pe un obiect luat din lista aia modifică și ce e în repository? Ce anume s-a copiat și ce nu?

2. La B2, de ce comparația `!byEmail.get().getId().equals(student.getId())` nu e suficientă, dar `other == student` este? În ce situație concretă cele două dau răspunsuri diferite?

3. `CourseRepository` compilează perfect, fără niciun warning, deși e complet nefuncțional (B3). Ce categorie de erori nu poate prinde compilatorul, principial — și ce ai fi putut face în 30 de secunde ca să prinzi asta singur, înainte de commit?

---

## Ordinea de lucru propusă

1. **B4 mai întâi** — decizia de id blochează tot restul (nu are rost să repari `add`-uri până nu știi ce id folosești).
2. **B3** — `loadData()` în constructor + CRUD-ul lipsă la `Course`.
3. **B2** — în ambele repository-uri.
4. **B1** — `Main` idempotent, care afișează starea.
5. **B5 + M1** — relațiile: întâi corect în memorie, apoi persistate.

Restul (🟡/🟢) după ce roșiile sunt închise.

---

**Regula care iese din runda asta:** un review se închide prin **rulare**, nu prin citire. B3 și B2 sunt invizibile la citit — codul arată corect în ambele cazuri. `Main`-ul tău nu apucă să atingă nici `Course`, nici `Book`, nici relațiile. Scrie-ți un `main` care exersează fiecare lucru pe care tocmai l-ai adăugat, și rulează-l **înainte** de commit.
