# LuigiBot UI Test Plan

Run these cases with the project-specific `test-ui` skill. Each case starts LuigiBot with an empty task list and compares the complete console output exactly.

## Test case: Add and list a Todo

**Aim:** Verify that LuigiBot adds a Todo and displays it in the task list.

### Input

```text
todo borrow book
list
bye
```

### Expected output

```text
____________________________________________________________
.____          .__       .____________        __   
|    |    __ __|__| ____ |__\______   \ _____/  |_
|    |   |  |  \  |/ ___\|  ||    |  _//  _ \   __\
|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | 
|_______ \____/|__\___  /|__||______  /\____/|__|
        \/       /_____/            \/             
____________________________________________________________
Its a-me,LuigiBot!
What can I do for you?
____________________________________________________________
____________________________________________________________
Okie-dokie! Luigi added this task:
  [T][ ] borrow book
You've-a got 1 tasks now!
____________________________________________________________
____________________________________________________________
Let's-a see what Luigi has on the list:
1.[T][ ] borrow book
____________________________________________________________
Mama mia! Leaving already? Cya soon!
____________________________________________________________
```

## Test case: Add and list a Deadline

**Aim:** Verify that LuigiBot stores and displays a Deadline with its `/by` value.

### Input

```text
deadline return book /by Sunday
list
bye
```

### Expected output

```text
____________________________________________________________
.____          .__       .____________        __   
|    |    __ __|__| ____ |__\______   \ _____/  |_
|    |   |  |  \  |/ ___\|  ||    |  _//  _ \   __\
|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | 
|_______ \____/|__\___  /|__||______  /\____/|__|
        \/       /_____/            \/             
____________________________________________________________
Its a-me,LuigiBot!
What can I do for you?
____________________________________________________________
____________________________________________________________
Okie-dokie! Luigi added this task:
  [D][ ] return book (by: Sunday)
You've-a got 1 tasks now!
____________________________________________________________
____________________________________________________________
Let's-a see what Luigi has on the list:
1.[D][ ] return book (by: Sunday)
____________________________________________________________
Mama mia! Leaving already? Cya soon!
____________________________________________________________
```

## Test case: Mark and unmark an Event

**Aim:** Verify that LuigiBot adds an Event and updates its status when marked and unmarked.

### Input

```text
event project meeting /from Mon 2pm /to 4pm
mark 1
list
unmark 1
bye
```

### Expected output

```text
____________________________________________________________
.____          .__       .____________        __   
|    |    __ __|__| ____ |__\______   \ _____/  |_
|    |   |  |  \  |/ ___\|  ||    |  _//  _ \   __\
|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | 
|_______ \____/|__\___  /|__||______  /\____/|__|
        \/       /_____/            \/             
____________________________________________________________
Its a-me,LuigiBot!
What can I do for you?
____________________________________________________________
____________________________________________________________
Okie-dokie! Luigi added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
You've-a got 1 tasks now!
____________________________________________________________
____________________________________________________________
Nice-a! Luigi marked this task as done:
  [E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Let's-a see what Luigi has on the list:
1.[E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
No problem! Luigi marked this task as not done:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
Mama mia! Leaving already? Cya soon!
____________________________________________________________
```

## Test case: Reject unknown command

**Aim:** Verify that LuigiBot reports an unknown command without storing it as a task.

### Input

```text
read book
list
bye
```

### Expected output

```text
____________________________________________________________
.____          .__       .____________        __   
|    |    __ __|__| ____ |__\______   \ _____/  |_
|    |   |  |  \  |/ ___\|  ||    |  _//  _ \   __\
|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | 
|_______ \____/|__\___  /|__||______  /\____/|__|
        \/       /_____/            \/             
____________________________________________________________
Its a-me,LuigiBot!
What can I do for you?
____________________________________________________________
____________________________________________________________
Oh no! Luigi doesn't-a recognize that command.
____________________________________________________________
____________________________________________________________
Let's-a see what Luigi has on the list:
____________________________________________________________
Mama mia! Leaving already? Cya soon!
____________________________________________________________
```

## Test case: Reject empty command

**Aim:** Verify that LuigiBot reports an empty command and continues accepting input.

### Input

```text

bye
```

### Expected output

```text
____________________________________________________________
.____          .__       .____________        __   
|    |    __ __|__| ____ |__\______   \ _____/  |_
|    |   |  |  \  |/ ___\|  ||    |  _//  _ \   __\
|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | 
|_______ \____/|__\___  /|__||______  /\____/|__|
        \/       /_____/            \/             
____________________________________________________________
Its a-me,LuigiBot!
What can I do for you?
____________________________________________________________
____________________________________________________________
Mamma mia! You didn't-a enter a command.
____________________________________________________________
Mama mia! Leaving already? Cya soon!
____________________________________________________________
```

## Test case: Reject malformed mark and unmark numbers

**Aim:** Verify that mark and unmark require a whole-number task index.

### Input

```text
mark one
unmark
bye
```

### Expected output

```text
____________________________________________________________
.____          .__       .____________        __   
|    |    __ __|__| ____ |__\______   \ _____/  |_
|    |   |  |  \  |/ ___\|  ||    |  _//  _ \   __\
|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | 
|_______ \____/|__\___  /|__||______  /\____/|__|
        \/       /_____/            \/             
____________________________________________________________
Its a-me,LuigiBot!
What can I do for you?
____________________________________________________________
____________________________________________________________
Mamma mia! Please-a enter a whole task number.
____________________________________________________________
____________________________________________________________
Mamma mia! Please-a enter a whole task number.
____________________________________________________________
Mama mia! Leaving already? Cya soon!
____________________________________________________________
```

## Test case: Reject out-of-range mark and unmark numbers

**Aim:** Verify that mark and unmark reject task numbers outside the stored task list.

### Input

```text
todo read book
mark 0
unmark 2
list
bye
```

### Expected output

```text
____________________________________________________________
.____          .__       .____________        __   
|    |    __ __|__| ____ |__\______   \ _____/  |_
|    |   |  |  \  |/ ___\|  ||    |  _//  _ \   __\
|    |___|  |  /  / /_/  >  ||    |   (  <_> )  | 
|_______ \____/|__\___  /|__||______  /\____/|__|
        \/       /_____/            \/             
____________________________________________________________
Its a-me,LuigiBot!
What can I do for you?
____________________________________________________________
____________________________________________________________
Okie-dokie! Luigi added this task:
  [T][ ] read book
You've-a got 1 tasks now!
____________________________________________________________
____________________________________________________________
Oh no! Luigi can't-a find that task number.
____________________________________________________________
____________________________________________________________
Oh no! Luigi can't-a find that task number.
____________________________________________________________
____________________________________________________________
Let's-a see what Luigi has on the list:
1.[T][ ] read book
____________________________________________________________
Mama mia! Leaving already? Cya soon!
____________________________________________________________
```
