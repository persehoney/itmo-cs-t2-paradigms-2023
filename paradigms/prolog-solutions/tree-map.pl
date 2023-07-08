get_key(vertice(Key, _, _, _), Key) :- !.
get_value(vertice(_, Value, _, _), Value) :- !.
get_left(vertice(_, _, Left, _), Left) :- !.
get_right(vertice(_, _, _, Right), Right) :- !.


map_get(vertice(Key, Value, _, _), Key, Value) :-  !.
map_get(TreeMap, TargetKey, Value) :-
        get_key(TreeMap, Key),
        get_left(TreeMap, Left),
        TargetKey < Key,
        map_get(Left, TargetKey, Value), !.
map_get(TreeMap, TargetKey, Value) :-
        get_key(TreeMap, Key),
        get_right(TreeMap, Right),
        Key < TargetKey,
        map_get(Right, TargetKey, Value), !.


map_build([], undefined):- !.
map_build(ListMap, vertice(Key, Value, Left, Right)):-
	    length(ListMap, Length),
	    list_split(ListMap, 0, Length, LeftTree, [(Key, Value) | RightTree]),
	    map_build(LeftTree, Left),
	    map_build(RightTree, Right), !.


list_split(_, P, Length, _, _) :- P >= Length, !.
list_split([], _, _, [], _) :- !.
list_split([], _, _, _, []) :- !.


list_split([V | TreeMap], P, Length, [V | Left], Right) :-
        P < Length // 2,
	    P1 is P + 1,
	    list_split(TreeMap, P1, Length, Left, Right), !.
list_split([V | TreeMap], P, Length, Left, [V | Right]) :-
	    P1 is P + 1,
	    list_split(TreeMap, P1, Length, Left, Right), !.


map_values(undefined, []) :- !.
map_values(Map, Values) :-
        get_left(Map, Left),
        get_right(Map, Right),
        get_value(Map, Value),

        map_values(Left, L1),
        append(L1, [Value], L2),
        map_values(Right, L3),
        append(L2, L3, Values).