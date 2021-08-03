package org.codi.lct.ds.extra;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuppressWarnings("checkstyle:MemberName")
public class Pair<L, R> {

    public L l;
    public R r;

    @Override
    public String toString() {
        return String.format("(%s,%s)", l, r);
    }
}
