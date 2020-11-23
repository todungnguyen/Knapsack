public enum Flag {
    CONTINUE, // continue sur ce noeud
    UNACHIEVABLE, // la solution de ce noeud est irréalisable
    SOLUTION, // ce noeud a une solution admissible <=> les valeurs dans la solution sont entiers (mais ce N'EST PAS FORCEMENT une solution optimale)
    UBLTLB // UB est plus petit que LB
}
