```
lee@lee:~/Codes/2023_2_sem$ gcc 20231030_lab01.c -Og -c
lee@lee:~/Codes/2023_2_sem$ gcc 20231030_lab01.c -Og -o lab01
```

```
objdump -d 20231030_lab01.o | less

20231030_lab01.o:     file format elf64-x86-64


Disassembly of section .text:

0000000000000000 <main>:
   0:   f3 0f 1e fa             endbr64 
   4:   48 83 ec 08             sub    $0x8,%rsp
   8:   48 8d 3d 00 00 00 00    lea    0x0(%rip),%rdi        # f <main+0xf>
   f:   e8 00 00 00 00          callq  14 <main+0x14>
  14:   b8 00 00 00 00          mov    $0x0,%eax
  19:   48 83 c4 08             add    $0x8,%rsp
  1d:   c3                      retq   

```
