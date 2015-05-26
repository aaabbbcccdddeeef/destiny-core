/**
 * Created by smallufo on 2015-05-26.
 */
package destiny.core.chinese;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class StemBranchOptional {

  @NotNull
  protected final Optional<Stem> stem;

  @NotNull
  protected final Optional<Branch> branch;

  // 0[甲子] ~ 59[癸亥]
  private transient static StemBranchOptional[] ARRAY = new StemBranchOptional[60];
  static {
    int n = 0;
    do {
      ARRAY[n] = new StemBranchOptional(
        Optional.of(Stem.getHeavenlyStems(n % 10)),
        Optional.of(Branch.getEarthlyBranches(n % 12))
      );
      n++;
    } while (n < 60);
  }


  StemBranchOptional(@NotNull Optional<Stem> stemOpt, @NotNull Optional<Branch> branchOpt) {
    check(stemOpt , branchOpt);
    this.stem = stemOpt;
    this.branch = branchOpt;
  }

  public static StemBranchOptional empty() {
    return new StemBranchOptional(Optional.empty() , Optional.empty());
  }


  /**
   * 0[甲子] ~ 59[癸亥]
   */
  private static StemBranchOptional get(int index) {
    return ARRAY[normalize(index)];
  }

  private static int normalize(int index)
  {
    if (index >= 60)
      return (normalize(index-60));
    else if (index < 0)
      return (normalize(index+60));
    else
      return index;
  }

  public static StemBranchOptional get(Optional<Stem> stemOpt, Optional<Branch> branchOpt) {
    check(stemOpt , branchOpt);

    if (stemOpt.isPresent() && branchOpt.isPresent()) {
      int hIndex = Stem.getIndex(stemOpt.get());
      int eIndex = Branch.getIndex(branchOpt.get());
      switch (hIndex - eIndex) {
        case 0:
        case -10:
          return get(eIndex);
        case 2:
        case -8:
          return get(eIndex + 12);
        case 4:
        case -6:
          return get(eIndex + 24);
        case 6:
        case -4:
          return get(eIndex + 36);
        case 8:
        case -2:
          return get(eIndex + 48);
        default:
          throw new AssertionError("Invalid Stem/Branch Combination!");
      }
    } else {
      return new StemBranchOptional(stemOpt , branchOpt);
    }
  }

  public static StemBranchOptional get(Stem stem, Branch branch) {
    return get(Optional.ofNullable(stem) , Optional.ofNullable(branch));
  }

  public static StemBranchOptional get(char stem, char branch) {
    return get(Optional.of(Stem.getHeavenlyStems(stem).get()), Optional.of(Branch.getEarthlyBranches(branch).get()));
  }

  public static StemBranchOptional get(@NotNull String stemBranch) {
    if (stemBranch.length() != 2)
      throw new RuntimeException("The length of " + stemBranch + " must equal to 2 !");
    else
      return get(stemBranch.charAt(0), stemBranch.charAt(1));
  }

  private static Optional<Integer> getIndex(StemBranchOptional sb) {
    if (sb.stem.isPresent() && sb.branch.isPresent()) {
      for(int i=0 ; i < ARRAY.length ; i++) {
        if (sb.equals(ARRAY[i])) {
          return Optional.of(i);
        }
      }
    }
    return Optional.empty();
  }

  public Optional<Integer> getIndexOpt() {
    return StemBranchOptional.getIndex(this);
  }

  public Optional<? extends StemBranchOptional> nextOpt(int n) {
    return getIndex(this).map(i -> get(i+n));
  }

  private static void check(@NotNull Optional<Stem> stemOpt, @NotNull Optional<Branch> branchOpt) {
    if (stemOpt.isPresent() && branchOpt.isPresent()) {
      if (stemOpt.get().getBooleanValue() !=  SimpleBranch.get(branchOpt.get()).getBooleanValue())
        throw new RuntimeException("Stem/Branch combination illegal ! " + stemOpt.get() + " cannot be combined with " + branchOpt.get());
    }
  }


  @NotNull
  public Optional<Stem> getStemOptional() {
    return stem;
  }

  @NotNull
  public Optional<Branch> getBranchOptional() {
    return branch;
  }

  @Override
  public String toString() {
    return "[" + stem + ' ' + branch + ']';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof StemBranchOptional))
      return false;
    StemBranchOptional that = (StemBranchOptional) o;
    return Objects.equals(stem, that.stem) && Objects.equals(branch, that.branch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stem, branch);
  }


}
