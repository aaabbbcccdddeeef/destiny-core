/**
 * Created by smallufo on 2019-01-23.
 */
package destiny.tools.converters

interface Encoder<T> {
  fun encode(instance: T): String
}

interface Decoder<T> {
  fun decode(encodedString : String) : T?
}

/** [Encoder] + [Decoder] */
interface Coder<T> : Encoder<T> , Decoder<T>