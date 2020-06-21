package jfftw;

public enum Flag {

    NO_TIMELIMIT(-1),
    MEASURE(0),
    DESTROY_INPUT(1),
    UNALIGNED(1 << 1),
    CONSERVE_MEMORY(1 << 2),
    EXHAUSTIVE(1 << 3),
    PRESERVE_INPUT(1 << 4),
    PATIENT(1 << 5),
    ESTIMATE(1 << 6),
    WISDOM_ONLY(1 << 21),
    ESTIMATE_PATIENT(1 << 7),
    BELIEVE_PCOST(1 << 8),
    NO_DFT_R2HC(1 << 9),
    NO_NONTHREADED(1 << 10),
    NO_BUFFERING(1 << 11),
    NO_INDIRECT_OP(1 << 12),
    ALLOW_LARGE_GENERIC(1 << 13),
    NO_RANK_SPLITS(1 << 14),
    NO_VRANK_SPLITS(1 << 15),
    NO_VRECURSE(1 << 16),
    NO_SIMD(1 << 17),
    NO_SLOW(1 << 18),
    NO_FIXED_RADIX_LARGE_N(1 << 19),
    ALLOW_PRUNING(1 << 20);

    public final int value;

    Flag(int v) {
        value = v;
    }

    public static int combine(Flag... flags) {
        int i = 0;
        for (Flag f : flags)
            i |= f.value;
        return i;
    }

}
