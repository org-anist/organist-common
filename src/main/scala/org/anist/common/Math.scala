package org.anist.common

object Math {
  /**
    * TODO: type parameterize with Numeric[T] (first attempts to do so failed)
    * References:
    * - http://stackoverflow.com/a/4058564
    * - http://stackoverflow.com/a/13670939
    */
  def euclideanNorm(xs: Traversable[Double]): Double = {
    math.sqrt(xs.map(x => math.pow(x, 2)).sum)
  }

  def cosineSimilarity(xs: Seq[Double], ys: Seq[Double]): Double = {
    // why not: a.zip(b).map(_*_).sum / (norm(a) * norm(b))
    // or even: a.zip(b).map((a, b) => a * b).sum / (norm(a) * norm(b))
    xs.zip(ys).map { case (x, y) => x * y }.sum / (euclideanNorm(xs) * euclideanNorm(ys))
  }

  /**
    * Find the Jaccard similarity (the size of the intersection divided by the size of the union) between two sets.
    *
    * @param a A set of elements of type T
    * @param b Another set of elements of type T (order of a and b does not matter)
    * @tparam T Can be any type, will be inferred from a and b, but a and b must have the same type.
    * @return A similarity measure between 0 and 1: 0 if the sets are completely discrete, 1 if they are identical, or somewhere in between; e.g., if they are the same length and share 2/3 of their elements, it will be 0.5
    */
  def jaccardSimilarity[T](a: Set[T], b: Set[T]): Double = {
    (a & b).size.toDouble / (a | b).size.toDouble
  }

  /**
    * First, let truePositives be the set of documents that were retrieved and were relevant:
    *   truePositives = relevant ∩ retrieved
    *
    * Precision is defined as: |truePositives| / |retrieved|
    *   I.e., what proportion of the retrieved documents were relevant?
    *
    * Recall is defined as: |truePositives| / |relevant|
    *   I.e., what proportion of the relevant documents were retrieved?
    *
    * In both cases, 1.0 is a "good" score, and 0.0 is a "bad" score.
    *
    * If the number of retrieved documents is 0, calculating the precision would be undefined.
    *   For the sake of usability, if both retrieved and relevant are 0, precision will be 1.0.
    *   Otherwise, if relevant > 0, precision will be 0.0
    *
    * Similarly, if the number of relevant documents is 0, calculating the recall would be undefined.
    *   If that's the case, but the number of retrieved documents is also 0, recall is 1.0.
    *   Otherwise, recall is 0.0.
    *
    * @param retrieved Set of documents predicted to be relevant by your prediction mechanism,
    *                  i.e., both true positives and false positives (should not have been retrieved but were)
    * @param relevant Set of documents that are actually relevant, based on the gold truth,
    *                 i.e., both true positives and false negatives (should have been retrieved but weren't)
    * @return Tuple of (precision, recall)
    */
  def measurePrecisionRecall[T](retrieved: Set[T], relevant: Set[T]): (Double, Double) = {
    // handle the special case of 0 documents retrieved and 0 documents recalled,
    // which doesn't require computing the intersection
    if (retrieved.isEmpty && relevant.isEmpty) {
      (1.0, 1.0)
    }
    else {
      // otherwise, at least one of precision / recall will require computing the intersection
      val truePositivesSize = (retrieved & relevant).size.toDouble
      val precision = if (retrieved.isEmpty) 0.0 else truePositivesSize / retrieved.size
      val recall = if (relevant.isEmpty) 0.0 else truePositivesSize / relevant.size
      (precision, recall)
    }
  }

  /**
    * Find the average of a series of numbers, whether they're decimals or integrals.
    *
    * Uses the solution from http://stackoverflow.com/a/6190665
    *
    * @param xs Series of numbers (with .sum and .size values)
    * @return A number with the same type as the input values
    */
  def mean[T : Numeric](xs: TraversableOnce[T]): T = implicitly[Numeric[T]] match {
    case num: Fractional[_] =>
      import num._
      xs.sum / fromInt(xs.size)
    case num: Integral[_] =>
      import num._
      xs.sum / fromInt(xs.size)
    case num =>
      sys.error(s"Indivisable numeric: $num")
  }
}
