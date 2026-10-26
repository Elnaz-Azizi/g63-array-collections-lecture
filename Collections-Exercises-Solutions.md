![Lexicon Logo](https://lexicongruppen.se/media/wi5hphtd/lexicon-logo.svg)

# Solutions – Java Collections Exercises

---

### Exercise 1 – Shopping List

**Problem:** Create an `ArrayList`, print items numbered, remove one by name, print again.

**What we used:** `ArrayList<String>` with a manual index loop and `remove(Object)`.

**Why:**
- `ArrayList` is the standard go-to list: ordered, allows duplicates, backed by an array.
- To print numbered items we need the index, so a regular `for` loop with `i` is cleaner than a for-each.
- `remove("Eggs")` removes by value (as an `Object`). This is an important distinction — `remove(2)` would remove by index (position 2), not by value `2`.

```java
List<String> shoppingList = new ArrayList<>();
shoppingList.add("Milk");
shoppingList.add("Bread");
shoppingList.add("Eggs");
shoppingList.add("Butter");
shoppingList.add("Coffee");

for (int i = 0; i < shoppingList.size(); i++) {
    IO.println("  " + (i + 1) + ". " + shoppingList.get(i));
}

shoppingList.remove("Eggs");  // remove by value, not by index
```

> **Note:** `list.remove(1)` removes the element at index 1.  
> `list.remove("Bread")` removes the element whose value is "Bread".  
> Both are valid method calls — Java picks the right one based on the argument type.

---

### Exercise 2 – Unique Cities

**Problem:** Given a list with duplicates, store only unique cities and print the count.

**What we used:** `HashSet<String>`, constructed directly from the list.

**Why:**
- A `Set` by definition cannot contain duplicate values. When you add a duplicate, it is silently ignored.
- `HashSet` gives O(1) (constant time) for both `add()` and `contains()` — the fastest option when order does not matter.
- Passing the list directly to the constructor is the shortest way to copy-and-deduplicate in one step.

```java
List<String> cities = Arrays.asList(
    "Stockholm", "Gothenburg", "Malmö", "Stockholm",
    "Uppsala", "Gothenburg", "Lund"
);

Set<String> uniqueCities = new HashSet<>(cities);  // duplicates dropped automatically

IO.println("Unique cities: " + uniqueCities);
IO.println("Total unique:  " + uniqueCities.size());
```

> **Note:** `HashSet` does not guarantee any specific order when printed. If you need alphabetical order, use `TreeSet` instead. If you need insertion order preserved, use `LinkedHashSet`.

---

### Exercise 3 – Phone Book

**Problem:** Store name → phone number pairs, accept a name from the user, look it up.

**What we used:** `HashMap<String, String>` and `IO.readln()` for user input.

**Why:**
- A phone book is a classic key-value mapping: name is the key, phone number is the value.
- `HashMap` gives O(1) lookup by key — exactly what we need for a "find by name" operation.
- We use `containsKey()` before `get()` to avoid a `NullPointerException` when the name is not in the map. Alternatively, `getOrDefault(name, "Contact not found.")` does the same in one line.

```java
Map<String, String> phoneBook = new HashMap<>();
phoneBook.put("Mehrdad Javan",  "0712345678");
phoneBook.put("Elnaz Azizi",    "0798765432");
phoneBook.put("Simon Elbrink",  "0789456123");
phoneBook.put("Anna Karlsson",  "0701122334");

String name = IO.readln("Enter a name to look up: ").trim();

if (phoneBook.containsKey(name)) {
    IO.println("Phone number: " + phoneBook.get(name));
} else {
    IO.println("Contact not found.");
}
```

> **Alternative one-liner:**
> ```java
> IO.println(phoneBook.getOrDefault(name, "Contact not found."));
> ```

---

### Exercise 4 – Find the Largest

**Problem:** Find the largest number in a list using a loop — no `Collections.max()`.

**What we used:** A single-pass loop seeded with the first element.

**Why:**
- The exercise forces you to understand the algorithm that `Collections.max()` runs internally.
- We seed `largest` with `numbers.get(0)` — the first element — not with `0`. If all numbers were negative, starting at `0` would produce the wrong answer.
- We then scan every element: if we find something bigger, it becomes the new `largest`.

```java
List<Integer> numbers = Arrays.asList(14, 3, 78, 45, 22, 91, 7, 56);

int largest = numbers.get(0);  // always seed with a real value from the list
for (int number : numbers) {
    if (number > largest) {
        largest = number;
    }
}
```

> **Why not seed with `Integer.MIN_VALUE`?** That also works, but seeding with the first element is cleaner — it makes no assumption about the value range.

---

### Exercise 5 – Count by Letter

**Problem:** Ask the user for a letter, count how many names start with it (case-insensitive).

**What we used:** `IO.readln()`, `Character.toUpperCase()` for normalisation.

**Why:**
- Case-insensitive comparison requires normalising both sides to the same case before comparing. Converting both to uppercase is the standard approach.
- `name.charAt(0)` extracts the first character as a `char`.
- `Character.toUpperCase()` works on individual characters — `String.toUpperCase()` works on the whole word. Both are valid; here we only need the first character.

```java
char letter = Character.toUpperCase(IO.readln("Enter a letter: ").trim().charAt(0));

int count = 0;
for (String name : names) {
    if (Character.toUpperCase(name.charAt(0)) == letter) {
        count++;
    }
}
```

> **Why `.trim()`?** If the user accidentally types a space before the letter, `trim()` removes it — otherwise `charAt(0)` would grab the space, not the letter.

---

### Exercise 6 – Word Frequency

**Problem:** Count how many times each word appears, then print sorted alphabetically.

**What we used:** `HashMap` for counting, then `TreeMap` for alphabetical output.

**Why:**
- `HashMap` is used to accumulate counts. `getOrDefault(word, 0) + 1` is the standard frequency-counting idiom: if the word has never been seen, default to 0 and add 1; otherwise increment the existing count.
- `HashMap` has no guaranteed order, so we wrap it in `new TreeMap<>(frequency)` at print time. `TreeMap` stores entries sorted by key — alphabetical for `String` keys.

```java
Map<String, Integer> frequency = new HashMap<>();
for (String word : words) {
    frequency.put(word, frequency.getOrDefault(word, 0) + 1);
}

// Wrap in TreeMap to get alphabetical order when printing
new TreeMap<>(frequency).forEach((word, count) ->
    IO.println("  " + word + ": " + count)
);
```

> **Pattern to remember:** `map.getOrDefault(key, 0) + 1` — the standard one-liner for frequency counting.  
> `new TreeMap<>(existingMap)` — the standard one-liner for printing a map in sorted key order.

---

### Exercise 7 – Remove Duplicates, Keep Order

**Problem:** Remove duplicates from a list while preserving the original order.

**What we used:** A `HashSet` as a "seen" tracker alongside an `ArrayList` for the result.

**Why:**
- `LinkedHashSet` would also work (it removes duplicates and preserves insertion order), but the two-structure approach makes the logic visible: the `HashSet` answers "have I seen this?", the `List` holds the result in original order.
- `set.add(number)` returns `true` if the value was new, `false` if it was already there. This lets us skip a separate `contains()` call — one check does both the lookup and the insert.

```java
Set<Integer> seen = new HashSet<>();
List<Integer> result = new ArrayList<>();

for (int number : numbers) {
    if (seen.add(number)) {   // add() returns false if already present
        result.add(number);
    }
}
```

> **Alternative:** `new LinkedHashSet<>(numbers)` produces the same result in one line, because `LinkedHashSet` preserves insertion order and drops duplicates automatically. The two-structure version is shown here to make the reasoning visible.

---

### Exercise 8 – Merge Without Duplicates

**Problem:** Merge two lists into one collection with no duplicates.

**What we used:** `HashSet` with two `addAll()` calls.

**Why:**
- Adding all elements from both lists into a `HashSet` performs a set *union*: elements that appear in either list end up in the result, duplicates are automatically ignored.
- This is equivalent to the mathematical A ∪ B operation.

```java
Set<Integer> merged = new HashSet<>();
merged.addAll(listA);
merged.addAll(listB);  // values already in the set are silently skipped
```

> **Note:** The result order is not guaranteed with `HashSet`. If you need the merged elements in a predictable order, wrap the result in `new TreeSet<>(merged)` (sorted) or `new LinkedHashSet<>()` (insertion order).

---

### Exercise 9 – Choose Your Collection: Submission Tracker

**Problem:** Track whether students have submitted. Reject duplicates. Only membership matters — not what they submitted, not the order.

**Collection chosen:** `HashSet<String>`

**Why:**
- The only question we ever ask is "has this student submitted?" — a pure membership check.
- `HashSet` answers `contains()` in O(1) time and automatically rejects duplicates.
- No ordering is needed, so the overhead of `LinkedHashSet` or `TreeSet` is unnecessary.
- `add()` returning `false` on a duplicate means we detect rejections for free — no separate `contains()` call needed.

```java
Set<String> submitted = new HashSet<>();

for (String student : attempts) {
    if (!submitted.add(student)) {   // false = already in the set
        IO.println("  Rejected: " + student + " has already submitted.");
    }
}
```

> **Design principle (YAGNI):** We don't store submission timestamps, grades, or files — those were not asked for. The simplest structure that answers the one question we have is always the right choice.

---

### Exercise 10 – Choose Your Collection: Concert Queue

**Problem:** Manage a first-in, first-out queue. People join the back; let in from the front.

**Collection chosen:** `Queue<String>` backed by `LinkedList`

**Why:**
- A queue has two defining operations: add to the back, remove from the front. This is FIFO — First In, First Out.
- `LinkedList` implements the `Queue` interface and gives O(1) for both `offer()` (add to back) and `poll()` (remove from front).
- We declare the variable as `Queue<String>` — not `LinkedList<String>`. Programming to the interface means the rest of the code works with any `Queue` implementation; `ArrayDeque` would be an equally valid drop-in.

```java
Queue<String> queue = new LinkedList<>();
queue.offer("Mehrdad");   // add to the back
queue.offer("Elnaz");
queue.offer("Simon");

queue.poll();   // remove and return the front element
queue.poll();
```

> **`offer()` vs `add()`:** Both add to the back. `add()` throws an exception if the queue has a capacity limit and is full; `offer()` returns `false` instead. For an unbounded `LinkedList`, they behave identically — `offer()` is preferred by convention.  
> **`poll()` vs `remove()`:** Both remove the front. `poll()` returns `null` if the queue is empty; `remove()` throws a `NoSuchElementException`. Prefer `poll()` to avoid surprises.

---

### Exercise 11 – Choose Your Collection: Student Grades

**Problem:** Store grades by name (fast lookup), then print all students sorted by grade (highest to lowest).

**Collection chosen:** `HashMap<String, Integer>` for storage; sort via `List<Map.Entry>`

**Why:**
- `HashMap` gives O(1) name-to-grade lookup — the primary operation.
- `HashMap` has no ordering, so we cannot sort it directly. We extract all entries into a `List<Map.Entry<String, Integer>>` and sort that list with a comparator.
- `b.getValue() - a.getValue()` sorts descending by grade: if B's grade is higher than A's, the result is positive, so B comes first.

```java
Map<String, Integer> grades = new HashMap<>();
grades.put("Alice",   92);
grades.put("Bob",     74);
grades.put("Charlie", 88);

// Direct lookup — O(1)
IO.println("Charlie's grade: " + grades.get("Charlie"));

// Sort by grade descending
List<Map.Entry<String, Integer>> entries = new ArrayList<>(grades.entrySet());
entries.sort((a, b) -> b.getValue() - a.getValue());

for (Map.Entry<String, Integer> entry : entries) {
    IO.println("  " + entry.getKey() + ": " + entry.getValue());
}
```

> **Why not just use `TreeMap`?** `TreeMap` sorts by *key* (name), not by *value* (grade). There is no built-in map that sorts by value, which is why extracting to a list and sorting is the standard approach.

---

### Exercise 12 – Group by First Letter

**Problem:** Group names by their first letter and print each group.

**What we used:** `HashMap<Character, List<String>>` with `computeIfAbsent()`, then `TreeMap` for printing.

**Why:**
- Each key (a letter) maps to a *list* of names, so the value type is `List<String>`.
- `computeIfAbsent(key, k -> new ArrayList<>())` is the clean way to handle grouping: if the key has never been seen, it creates a new empty list and stores it; either way it returns the list, so we can immediately call `.add()` on it.
- Without `computeIfAbsent` you would need an `if (map.containsKey(key))` block before every add — more code, same result.

```java
Map<Character, List<String>> groups = new HashMap<>();

for (String name : names) {
    char key = name.charAt(0);
    groups.computeIfAbsent(key, k -> new ArrayList<>()).add(name);
}

// Print in alphabetical key order
new TreeMap<>(groups).forEach((letter, nameList) ->
    IO.println("  " + letter + ": " + nameList)
);
```

> **`computeIfAbsent` in plain English:** "If this key isn't in the map yet, run the function to create a value for it, put it in, then return the value. Otherwise just return the existing value."

---

### Exercise 13 – Most Frequent Word

**Problem:** Given a word list, find the single most frequently occurring word.

**What we used:** `HashMap` for frequency counts, then a single pass over `entrySet()` to find the max.

**Why:**
- Building a frequency map is step one (same pattern as Exercise 6).
- Finding the maximum does not require sorting all entries — a single linear scan comparing counts is O(n) and sufficient.
- We track `mostFrequent` (the word) and `maxCount` (its count) together: whenever a new entry beats the current max, both variables are updated at once.

```java
Map<String, Integer> frequency = new HashMap<>();
for (String word : words) {
    frequency.put(word, frequency.getOrDefault(word, 0) + 1);
}

String mostFrequent = null;
int maxCount = 0;
for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
    if (entry.getValue() > maxCount) {
        maxCount       = entry.getValue();
        mostFrequent   = entry.getKey();
    }
}
```

> **Why not sort?** Sorting all entries to find the maximum is O(n log n). A linear scan is O(n) and is always faster for finding one extreme value. Only sort when you need the full ranking.

---

### Exercise 14 – Common Elements

**Problem:** Find all elements that appear in both lists, with no duplicates in the result.

**What we used:** Load List A into a `HashSet` for O(1) lookup; iterate List B and collect matches into a second `HashSet`.

**Why:**
- If we used `listA.contains(number)` inside the loop over List B, each `contains()` call would scan the entire List A — O(n) per call, O(n²) total.
- Loading List A into a `HashSet` first reduces each lookup to O(1), making the overall algorithm O(n).
- Collecting results into a `HashSet` ensures uniqueness automatically, even if an element appears multiple times in either list.
- This operation is a set *intersection* (A ∩ B).

```java
Set<Integer> setA = new HashSet<>(listA);   // O(n) setup
Set<Integer> common = new HashSet<>();

for (int number : listB) {
    if (setA.contains(number)) {             // O(1) lookup
        common.add(number);                  // duplicates ignored automatically
    }
}
```

> **Set operations summary:**  
> - Union (A ∪ B): `setA.addAll(setB)`  
> - Intersection (A ∩ B): `setA.retainAll(setB)` — or the explicit loop above  
> - Difference (A − B): `setA.removeAll(setB)`

---

### Exercise 15 – Two Sum

**Problem:** Find two numbers in a list that add up to a target. Avoid a nested loop.

**What we used:** `HashMap<Integer, Integer>` mapping each value to its index — the "complement lookup" pattern.

**Why:**
- The naive approach (two nested loops) is O(n²): for every element, scan all remaining elements.
- The key insight: if we are looking for two numbers that sum to `target`, and we know `current`, then we need `target - current`. We call this the *complement*.
- As we walk the list, we record every number we have seen in the map. For each new number, we check: is its complement already in the map? If yes, we found our pair. One pass → O(n).

```java
Map<Integer, Integer> seen = new HashMap<>();  // value → index

for (int i = 0; i < numbers.size(); i++) {
    int current    = numbers.get(i);
    int complement = target - current;

    if (seen.containsKey(complement)) {
        IO.println("Found: " + complement + " + " + current + " = " + target);
        return;
    }
    seen.put(current, i);
}
```

> **Why store the index?** Storing the index lets you verify that the pair uses two *different* elements (not the same element twice). For example, with target `4` and a list containing `2`, you do not want to pair `2` with itself.  
> **Time complexity:** O(n) — each element is visited once.

---

### Exercise 16 – Second Largest

**Problem:** Find the second largest *unique* value in a list. No sorting allowed.

**What we used:** Two variables (`first` and `second`) updated in a single pass.

**Why:**
- Sorting would give us the answer, but it costs O(n log n) and the exercise explicitly forbids it — the point is to understand how to find order statistics in a single scan.
- We maintain the two highest unique values seen so far. When a new number beats `first`, the old `first` drops to `second`. When it beats only `second` (and is not equal to `first`), only `second` is updated.
- Seeding with `Integer.MIN_VALUE` ensures any real value in the list will be larger than the seed.

```java
int first  = Integer.MIN_VALUE;
int second = Integer.MIN_VALUE;

for (int number : numbers) {
    if (number > first) {
        second = first;   // old champion becomes runner-up
        first  = number;
    } else if (number > second && number != first) {
        second = number;  // new runner-up (must be different value from first)
    }
}
```

> **Why `number != first`?** The list contains `91` twice and `78` twice. Without this check, the second occurrence of `91` would incorrectly register as the second-largest. The `!= first` guard ensures `second` holds a *different* value, not just a lower duplicate.

---

### Exercise 17 – Top 3 Most Frequent

**Problem:** Find the 3 most frequently occurring words, ordered from most to least frequent.

**What we used:** `HashMap` for frequency counts, then extract to a `List<Map.Entry>`, sort descending, take top 3.

**Why:**
- Building the frequency map is the same pattern as Exercises 6 and 13.
- To rank all words we need a sorted view. We extract map entries into a list and sort by count descending.
- `Math.min(3, entries.size())` makes the loop safe if the list has fewer than 3 distinct words.

```java
Map<String, Integer> frequency = new HashMap<>();
for (String word : words) {
    frequency.put(word, frequency.getOrDefault(word, 0) + 1);
}

List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequency.entrySet());
entries.sort((a, b) -> b.getValue() - a.getValue());  // descending by count

for (int i = 0; i < Math.min(3, entries.size()); i++) {
    Map.Entry<String, Integer> entry = entries.get(i);
    IO.println("  " + (i + 1) + ". " + entry.getKey() + " (" + entry.getValue() + ")");
}
```

> **Comparator pattern:** `(a, b) -> b.getValue() - a.getValue()` sorts descending.  
> `(a, b) -> a.getValue() - b.getValue()` sorts ascending.  
> The sign of the result tells Java the order: negative = a comes first, positive = b comes first, zero = equal.

---

## Quick Reference – Which Collection to Pick?

| Scenario | Collection | Why |
|---|---|---|
| Ordered list, duplicates allowed | `ArrayList` | Fast random access by index |
| FIFO queue | `Queue` / `LinkedList` | O(1) add to back, remove from front |
| Membership check only, no order | `HashSet` | O(1) add and contains, rejects duplicates |
| Membership check, preserve insert order | `LinkedHashSet` | Same as HashSet but ordered |
| Membership check, always sorted | `TreeSet` | Sorted automatically, O(log n) operations |
| Key → value lookup | `HashMap` | O(1) get and put |
| Key → value, sorted by key | `TreeMap` | Keys always in natural sort order |
| Key → value, preserve insert order | `LinkedHashMap` | Iteration follows insertion order |
| Group items by a key | `HashMap<K, List<V>>` + `computeIfAbsent` | Standard grouping pattern |
| Frequency count | `HashMap<K, Integer>` + `getOrDefault` | Standard counting pattern |
