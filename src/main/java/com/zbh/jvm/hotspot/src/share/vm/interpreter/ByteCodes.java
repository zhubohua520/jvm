package com.zbh.jvm.hotspot.src.share.vm.interpreter;

public class ByteCodes {

    public static final int _illegal = -1;

    // Java bytecodes
    public static final int _nop = 0; // 0x00
    public static final int _aconst_null = 1; // 0x01
    public static final int _iconst_m1 = 2; // 0x02
    public static final int _iconst_0 = 3; // 0x03
    public static final int _iconst_1 = 4; // 0x04
    public static final int _iconst_2 = 5; // 0x05
    public static final int _iconst_3 = 6; // 0x06
    public static final int _iconst_4 = 7; // 0x07
    public static final int _iconst_5 = 8; // 0x08
    public static final int _lconst_0 = 9; // 0x09
    public static final int _lconst_1 = 10; // 0x0a
    public static final int _fconst_0 = 11; // 0x0b
    public static final int _fconst_1 = 12; // 0x0c
    public static final int _fconst_2 = 13; // 0x0d
    public static final int _dconst_0 = 14; // 0x0e
    public static final int _dconst_1 = 15; // 0x0f
    public static final int _bipush = 16; // 0x10
    public static final int _sipush = 17; // 0x11
    public static final int _ldc = 18; // 0x12
    public static final int _ldc_w = 19; // 0x13
    public static final int _ldc2_w = 20; // 0x14
    public static final int _iload = 21; // 0x15
    public static final int _lload = 22; // 0x16
    public static final int _fload = 23; // 0x17
    public static final int _dload = 24; // 0x18
    public static final int _aload = 25; // 0x19
    public static final int _iload_0 = 26; // 0x1a
    public static final int _iload_1 = 27; // 0x1b
    public static final int _iload_2 = 28; // 0x1c
    public static final int _iload_3 = 29; // 0x1d
    public static final int _lload_0 = 30; // 0x1e
    public static final int _lload_1 = 31; // 0x1f
    public static final int _lload_2 = 32; // 0x20
    public static final int _lload_3 = 33; // 0x21
    public static final int _fload_0 = 34; // 0x22
    public static final int _fload_1 = 35; // 0x23
    public static final int _fload_2 = 36; // 0x24
    public static final int _fload_3 = 37; // 0x25
    public static final int _dload_0 = 38; // 0x26
    public static final int _dload_1 = 39; // 0x27
    public static final int _dload_2 = 40; // 0x28
    public static final int _dload_3 = 41; // 0x29
    public static final int _aload_0 = 42; // 0x2a
    public static final int _aload_1 = 43; // 0x2b
    public static final int _aload_2 = 44; // 0x2c
    public static final int _aload_3 = 45; // 0x2d
    public static final int _iaload = 46; // 0x2e
    public static final int _laload = 47; // 0x2f
    public static final int _faload = 48; // 0x30
    public static final int _daload = 49; // 0x31
    public static final int _aaload = 50; // 0x32
    public static final int _baload = 51; // 0x33
    public static final int _caload = 52; // 0x34
    public static final int _saload = 53; // 0x35
    public static final int _istore = 54; // 0x36
    public static final int _lstore = 55; // 0x37
    public static final int _fstore = 56; // 0x38
    public static final int _dstore = 57; // 0x39
    public static final int _astore = 58; // 0x3a
    public static final int _istore_0 = 59; // 0x3b
    public static final int _istore_1 = 60; // 0x3c
    public static final int _istore_2 = 61; // 0x3d
    public static final int _istore_3 = 62; // 0x3e
    public static final int _lstore_0 = 63; // 0x3f
    public static final int _lstore_1 = 64; // 0x40
    public static final int _lstore_2 = 65; // 0x41
    public static final int _lstore_3 = 66; // 0x42
    public static final int _fstore_0 = 67; // 0x43
    public static final int _fstore_1 = 68; // 0x44
    public static final int _fstore_2 = 69; // 0x45
    public static final int _fstore_3 = 70; // 0x46
    public static final int _dstore_0 = 71; // 0x47
    public static final int _dstore_1 = 72; // 0x48
    public static final int _dstore_2 = 73; // 0x49
    public static final int _dstore_3 = 74; // 0x4a
    public static final int _astore_0 = 75; // 0x4b
    public static final int _astore_1 = 76; // 0x4c
    public static final int _astore_2 = 77; // 0x4d
    public static final int _astore_3 = 78; // 0x4e
    public static final int _iastore = 79; // 0x4f
    public static final int _lastore = 80; // 0x50
    public static final int _fastore = 81; // 0x51
    public static final int _dastore = 82; // 0x52
    public static final int _aastore = 83; // 0x53
    public static final int _bastore = 84; // 0x54
    public static final int _castore = 85; // 0x55
    public static final int _sastore = 86; // 0x56
    public static final int _pop = 87; // 0x57
    public static final int _pop2 = 88; // 0x58
    public static final int _dup = 89; // 0x59
    public static final int _dup_x1 = 90; // 0x5a
    public static final int _dup_x2 = 91; // 0x5b
    public static final int _dup2 = 92; // 0x5c
    public static final int _dup2_x1 = 93; // 0x5d
    public static final int _dup2_x2 = 94; // 0x5e
    public static final int _swap = 95; // 0x5f
    public static final int _iadd = 96; // 0x60
    public static final int _ladd = 97; // 0x61
    public static final int _fadd = 98; // 0x62
    public static final int _dadd = 99; // 0x63
    public static final int _isub = 100; // 0x64
    public static final int _lsub = 101; // 0x65
    public static final int _fsub = 102; // 0x66
    public static final int _dsub = 103; // 0x67
    public static final int _imul = 104; // 0x68
    public static final int _lmul = 105; // 0x69
    public static final int _fmul = 106; // 0x6a
    public static final int _dmul = 107; // 0x6b
    public static final int _idiv = 108; // 0x6c
    public static final int _ldiv = 109; // 0x6d
    public static final int _fdiv = 110; // 0x6e
    public static final int _ddiv = 111; // 0x6f
    public static final int _irem = 112; // 0x70
    public static final int _lrem = 113; // 0x71
    public static final int _frem = 114; // 0x72
    public static final int _drem = 115; // 0x73
    public static final int _ineg = 116; // 0x74
    public static final int _lneg = 117; // 0x75
    public static final int _fneg = 118; // 0x76
    public static final int _dneg = 119; // 0x77
    public static final int _ishl = 120; // 0x78
    public static final int _lshl = 121; // 0x79
    public static final int _ishr = 122; // 0x7a
    public static final int _lshr = 123; // 0x7b
    public static final int _iushr = 124; // 0x7c
    public static final int _lushr = 125; // 0x7d
    public static final int _iand = 126; // 0x7e
    public static final int _land = 127; // 0x7f
    public static final int _ior = 128; // 0x80
    public static final int _lor = 129; // 0x81
    public static final int _ixor = 130; // 0x82
    public static final int _lxor = 131; // 0x83
    public static final int _iinc = 132; // 0x84
    public static final int _i2l = 133; // 0x85
    public static final int _i2f = 134; // 0x86
    public static final int _i2d = 135; // 0x87
    public static final int _l2i = 136; // 0x88
    public static final int _l2f = 137; // 0x89
    public static final int _l2d = 138; // 0x8a
    public static final int _f2i = 139; // 0x8b
    public static final int _f2l = 140; // 0x8c
    public static final int _f2d = 141; // 0x8d
    public static final int _d2i = 142; // 0x8e
    public static final int _d2l = 143; // 0x8f
    public static final int _d2f = 144; // 0x90
    public static final int _i2b = 145; // 0x91
    public static final int _i2c = 146; // 0x92
    public static final int _i2s = 147; // 0x93
    public static final int _lcmp = 148; // 0x94
    public static final int _fcmpl = 149; // 0x95
    public static final int _fcmpg = 150; // 0x96
    public static final int _dcmpl = 151; // 0x97
    public static final int _dcmpg = 152; // 0x98
    public static final int _ifeq = 153; // 0x99
    public static final int _ifne = 154; // 0x9a
    public static final int _iflt = 155; // 0x9b
    public static final int _ifge = 156; // 0x9c
    public static final int _ifgt = 157; // 0x9d
    public static final int _ifle = 158; // 0x9e
    public static final int _if_icmpeq = 159; // 0x9f
    public static final int _if_icmpne = 160; // 0xa0
    public static final int _if_icmplt = 161; // 0xa1
    public static final int _if_icmpge = 162; // 0xa2
    public static final int _if_icmpgt = 163; // 0xa3
    public static final int _if_icmple = 164; // 0xa4
    public static final int _if_acmpeq = 165; // 0xa5
    public static final int _if_acmpne = 166; // 0xa6
    public static final int _goto = 167; // 0xa7
    public static final int _jsr = 168; // 0xa8
    public static final int _ret = 169; // 0xa9
    public static final int _tableswitch = 170; // 0xaa
    public static final int _lookupswitch = 171; // 0xab
    public static final int _ireturn = 172; // 0xac
    public static final int _lreturn = 173; // 0xad
    public static final int _freturn = 174; // 0xae
    public static final int _dreturn = 175; // 0xaf
    public static final int _areturn = 176; // 0xb0
    public static final int _return = 177; // 0xb1
    public static final int _getstatic = 178; // 0xb2
    public static final int _putstatic = 179; // 0xb3
    public static final int _getfield = 180; // 0xb4
    public static final int _putfield = 181; // 0xb5
    public static final int _invokevirtual = 182; // 0xb6
    public static final int _invokespecial = 183; // 0xb7
    public static final int _invokestatic = 184; // 0xb8
    public static final int _invokeinterface = 185; // 0xb9
    public static final int _invokedynamic = 186; // 0xba     // if EnableInvokeDynamic
    public static final int _new = 187; // 0xbb
    public static final int _newarray = 188; // 0xbc
    public static final int _anewarray = 189; // 0xbd
    public static final int _arraylength = 190; // 0xbe
    public static final int _athrow = 191; // 0xbf
    public static final int _checkcast = 192; // 0xc0
    public static final int _instanceof = 193; // 0xc1
    public static final int _monitorenter = 194; // 0xc2
    public static final int _monitorexit = 195; // 0xc3
    public static final int _wide = 196; // 0xc4
    public static final int _multianewarray = 197; // 0xc5
    public static final int _ifnull = 198; // 0xc6
    public static final int _ifnonnull = 199; // 0xc7
    public static final int _goto_w = 200; // 0xc8
    public static final int _jsr_w = 201; // 0xc9
    public static final int _breakpoint = 202; // 0xca

}