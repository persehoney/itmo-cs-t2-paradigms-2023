build_composite(N, _, MAX_N) :-
    N > MAX_N, !.
build_composite(_, I, _) :-
    composite(I), !.
build_composite(N, I, MAX_N) :-
    NI is N + I,
    assert(composite(NI)),
    build_composite(NI, I, MAX_N), !.

init(N, _, MAX_N) :-
    N > MAX_N, !.

init(N, P, MAX_N) :-
    build_composite(N, N, MAX_N),
    init(N + 2, R, MAX_N).

init(MAX_N) :-
    build_composite(2, 2, MAX_N),
    init(3, 2, MAX_N).

prime(N) :- \+ composite(N), !.

is_least_divisor(N, I, I) :- 0 is N mod I, !.
is_least_divisor(N, I, R) :-
    I1 is I + 1,
    is_least_divisor(N, I1, R).

prime_divisors(1, []) :- !.
prime_divisors(N, [R | Divisors]) :-
    is_least_divisor(N, 2, R),
    Rest_Divisor is N // R,
    prime_divisors(Rest_Divisor, Divisors).

cube_divisors(1, []) :- !.

cube_divisors(N, D) :-
    prime_divisors(N * N * N, D).