public enum ReservedWords {
    Assign      ("=", 1),
    Semicolon   (";", 2),
    LPar        ("(", 3),
    RPar        (")", 4),
    LSquare     ("[", 5),
    RSquare     ("]", 6),
    LBrace      ("{", 7),
    RBrace      ("}", 8),
    Plus        ("+", 9),
    Minus       ("-",10),
    Mult        ("*", 11),
    Div         ("/", 12),
    Mod         ("%", 13),
    Lt          ("<", 14),
    Gt          (">", 15),
    Eq          ("==", 16),
    If          ("if", 17),
    Else        ("else", 18),
    While       ("while", 19),
    Break       ("break", 20),
    Continue    ("continue", 21),
    Return      ("return", 22),
    Comma       (",", 23),
    Const       ("const", 24),
    Int         ("int", 25),
    Void        ("void", 26),
    Main        ("main", 27);


    String name;
    int i;
    int index = 26;

    ReservedWords(String s, int i) {
        this.name = s;
        this.i = i;
    }

    public String getName() {
        return name;
    }

    public int getI() {
        return i;
    }

}
