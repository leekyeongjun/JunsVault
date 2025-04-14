
# Registers
- 31 Registers (64 bits)
	- `Zero` : hardwired
	- `ra` : return address
		- Return addr is not normally pushed onto the stack
	- `sp` : stack pointer
	- `tp` : thread pointer 
		- Contains Core Number
	- `gp` : global pointer
		- Used by compiler
	- `a0~a7` : arguments for functions
		- `a0` : return value
	- `t0~t6` : temporary / working registers
	- `s0~s11` : callee saved registers
- `pc` (program counter)

At each context switch
- Save State of previous thread.
- Load state of next thread.

# Modes
- Machine mode (M)
	- Highest, most powerful
	- Initial mode, after power on / reset
	- not used very much
		- only in Startup + initialization
	- Timer interrupts require machine mode
		- immedieatly converts it into an interrupt to supervisor mode.
		- Handler is short and quickly returns.
- Kernel mode (S)
	- All kernel code runs in this mode
- User mode (U)
	- All user code runs in this mode.
	- previliged instructions cause trap + abort.

# Control and Status Registers (CSRs)
xv6 only 19 of CSRs are important.
Privileged Instructions
```
csrr a0, sstatus (read)
csrw sstatus, a0 (write)
csrrw a0, mscratch, a0 (swap atomic)
```

| machine  | supervisor | explain                  |
| -------- | ---------- | ------------------------ |
| mhartid  |            | core Id                  |
| mstatus  | sstatus    | Status register          |
| mtvec    | stvec      | Trap vector/Handler addr |
| mepc     | sepc       | Previous PC              |
|          | scause     |                          |
| mscratch | sscratch   | Work reg                 |
|          | satp       | Addr Translation Ptr     |
| mie      | sie        | Interrupt enable         |
|          | sip        | Interrupt pending        |
| medeleg  |            | Exception Delegation     |
| mideleg  |            | Interrupt Delegation     |
