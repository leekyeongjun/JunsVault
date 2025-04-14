```C
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#define NUM_THREADS     8

char *messages[NUM_THREADS];

void *PrintHello(void *threadid)
{
    long taskid;

    sleep(1);
    taskid = (long) threadid;
    printf("Thread %ld: %s\n", taskid, messages[taskid]);
    pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
    pthread_t threads[NUM_THREADS];
    long taskids[NUM_THREADS];
    int rc, t;

    // Initialize global variable
    messages[0] = "English: Hello World!";
    messages[1] = "French: Bonjour, le monde!";
    messages[2] = "Spanish: Hola al mundo";
    messages[3] = "Klingon: Nuq neH!";
    messages[4] = "German: Guten Tag, Welt!"; 
    messages[5] = "Russian: Zdravstvuyte, mir!";
    messages[6] = "Korea: Ahnneyonghaseyo segye!";
    messages[7] = "Latin: Orbis, te saluto!";

    for(t = 0; t < NUM_THREADS; t++) {
        taskids[t] = t;
        printf("Creating thread %d\n", t);
        rc = pthread_create(&threads[t], NULL, PrintHello, (void *) taskids[t]);
        if (rc) {
        printf("ERROR; return code from pthread_create() is %d\n", rc);
        exit(-1);
        }
    }

    pthread_exit(NULL);
}

```

```C
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#define NUM_THREADS     8

char *messages[NUM_THREADS];

struct thread_data
{
    int  thread_id;
    int  sum;
    char *message;
};

struct thread_data thread_data_array[NUM_THREADS];

void *PrintHello(void *threadarg)
{
    int taskid, sum;
    char *hello_msg;
    struct thread_data *my_data;

    sleep(1);
    my_data = (struct thread_data *) threadarg;
    taskid = my_data->thread_id;
    sum = my_data->sum;
    hello_msg = my_data->message;
    printf("Thread %d: %s  Sum = %d\n", taskid, hello_msg, sum);
    pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
    pthread_t threads[NUM_THREADS];
    int *taskids[NUM_THREADS];
    int rc, t, sum;

    sum = 0;
    messages[0] = "English: Hello World!";
    messages[1] = "French: Bonjour, le monde!";
    messages[2] = "Spanish: Hola al mundo";
    messages[3] = "Klingon: Nuq neH!";
    messages[4] = "German: Guten Tag, Welt!"; 
    messages[5] = "Russian: Zdravstvytye, mir!";
    messages[6] = "Korea: Ahnneyonghaseyo segye!";
    messages[7] = "Latin: Orbis, te saluto!";

    for(t = 0; t < NUM_THREADS; t++) {
        sum = sum + t;
        thread_data_array[t].thread_id = t;
        thread_data_array[t].sum = sum;
        thread_data_array[t].message = messages[t];
        printf("Creating thread %d\n", t);
        rc = pthread_create(&threads[t], NULL, PrintHello,
                            (void *)&thread_data_array[t]);
        if (rc) {
            printf("ERROR; return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }

    pthread_exit(NULL);
}

```

```C
#include <pthread.h>
#include <stdio.h>
#define NUM_THREADS    5

void *PrintHello(void *threadid) {
    long tid;
    tid = (long)threadid;
    printf("Hello World! It's me, thread #%ld!\n", tid);
    pthread_exit(NULL);
}

int main (int argc, char *argv[]) {
    pthread_t threads[NUM_THREADS];
    int rc;
    long t;
    for(t = 0; t < NUM_THREADS; t++) {
        rc = pthread_create(&threads[t], NULL, PrintHello, (void *)t);
        printf("In main: creating thread %ld thread id: %ld\n", t, threads[t]);
        if (rc) {
            printf("ERROR; return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }
 
    pthread_exit(NULL);
}

```

```C
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#define NUM_THREADS    4

void *BusyWork(void *t)
{
    int i;
    long tid;
    double result = 0.0;
    tid = (long)t;
    printf("Thread %ld starting...\n", tid);
    for (i = 0; i < 1000000; i++)
    {
        result += i;
    }
    printf("Thread %ld done. Result = %e\n", tid, result);
    pthread_exit((void*) t);
}

int main (int argc, char *argv[])
{
    pthread_t thread[NUM_THREADS];
    int rc;
    long t;
    void *status;

    for(t = 0; t < NUM_THREADS; t++) {
        printf("Main: creating thread %ld\n", t);
        rc = pthread_create(&thread[t], NULL, BusyWork, (void *)t); 
        if (rc) {
            printf("ERROR; return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }

    for(t = 0; t < NUM_THREADS; t++) {
        rc = pthread_join(thread[t], &status);
        if (rc) {
            printf("ERROR; return code from pthread_join() is %d\n", rc);
            exit(-1);
        }
        printf("Main: completed join with thread %ld having a status of %ld\n", t, (long) status);
    }

    printf("Main: program completed. Exiting.\n");
    pthread_exit(NULL);
}


```