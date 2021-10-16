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
    Mult        ("*", 10),
    Div         ("/", 11),
    Lt          ("<", 12),
    Gt          (">", 13),
    Eq          ("==", 14),
    If          ("if", 15),
    Else        ("else", 16),
    While       ("while", 17),
    Break       ("break", 18),
    Continue    ("continue", 19),
    Return      ("return", 20);
    String name;
    int i;
    int index = 21;

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
