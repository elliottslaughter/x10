/*
 * (c) Copyright IBM Corporation 2007
 *
 * $Id: switch.h,v 1.8 2007-10-19 16:04:29 ganeshvb Exp $
 * This file is part of X10 Runtime System.
 */

/** X10Lib's Switch interface. **/

#ifndef __X10_SWITCH_H
#define __X10_SWITCH_H


/* x10 waiting categories (unused for the moment) */
typedef enum {
	X10_BOTH_WAIT = 0, /* Wait on both send and receive completion */
	X10_SEND_WAIT, /* Wait on send completion */
	X10_RECV_WAIT, /* Wait on receive completion */
} x10_wait_type_t;


/* C++ Lang Interface */
#ifdef __cplusplus
namespace x10lib {
extern int __x10_num_places;
extern int __x10_my_place;

class Switch {
public:
	Switch(int val = 0);
	~Switch();
	void* get_handle(void);
	void decrement(void);
	void next(void);
private:
	void *cntrp;
};

} /* closing brace for namespace x10lib */

typedef x10lib::Switch *x10_switch_t;

namespace x10lib {

/* Allocate switch object and return reference to it. */
x10_switch_t AllocSwitch(void);

/* Deallocate switch object. */
void FreeSwitch(x10_switch_t sw);

} /* closing brace for namespace x10lib */
#endif

/* C Lang Interface */
#ifdef __cplusplus
extern "C" {
#else
struct Switch;
typedef struct Switch* x10_switch_t;
#endif

/* Allocate switch object and return reference to it. */
x10_switch_t x10_alloc_switch(void);

/* Deallocate switch object. */
void x10_free_switch(x10_switch_t sw);

/* Reset switch before waiting once again. */
void x10_decrement_switch(x10_switch_t sw);

/* Perform wait on switch. */
void x10_next_on_switch(x10_switch_t sw);

/* return the underlying counter */
void* x10_switch_get_handle (x10_switch_t sw);

#ifdef __cplusplus
} /* closing brace for extern "C" */
#endif

#endif /* __X10_SWITCH_H */
