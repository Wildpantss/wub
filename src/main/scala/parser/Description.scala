package com.wildpants.wub
package parser

import parser.Description.DescItem
import console.Style.*

import scala.annotation.targetName
import scala.collection.mutable.ListBuffer
import scala.util.Properties.lineSeparator as endl

/**
 * = Description =
 *
 * Description text for [[Command]].
 *
 * == Construction examples ==
 *
 * Few examples showing how to build / initialize instance of [[Description]].
 *
 * === 1. build empty one with apply method ===
 * {{{
 * val desc = Description()
 * }}}
 *
 * === 2. build with only 1 simple paragraph, using apply method ===
 * {{{
 * val desc = Description("simple para content ...")
 * }}}
 *
 * === 3. build with complex elements, using apply method ===
 * {{{
 * val desc = Description(
 *              header("Header"),
 *              para("This is the para 1 ..."),
 *              uList("item1", "item2"),
 *              oList("i1", "i2"),
 *            )
 * }}}
 *
 * === 4. append items with += operator ===
 * {{{
 *   val desc = Description()
 *              += header("Header")
 *              += para("This is the para 1 ...")
 *              += uList("item1", "item2")
 *              += oList("i1", "i2")
 * }}}
 *
 * === 5. append items with ++= operator ===
 * {{{
 *   val desc = Description()
 *              ++= Seq(
 *                header("Header"),
 *                para("This is the para 1 ..."),
 *                uList("item1", "item2"),
 *                oList("i1", "i2"),
 *              )
 * }}}
 *
 * @note you could use multiple ways to build a instance, and call [[toString]] to transform it into [[String]].
 * @author wildpants
 * */
//noinspection ScalaWeakerAccess
class Description {

  private val contents: ListBuffer[DescItem] = ListBuffer.empty[DescItem] // content-holder

  /**
   * Alias for [[addItem]]
   * */
  @targetName("+=") // target-name alias not needed.
  def +=[T <: DescItem](item: T): Description = addItem(item)

  /**
   * Alias for [[addItems]]
   * */
  @targetName("++=") // target-name alias not needed.
  def ++=[T <: DescItem](items: Seq[T]): Description = addItems(items)

  /**
   * == Description.addItem ==
   *
   * Append an item into the [[Description]].
   *
   * @tparam T a subtype of [[DescItem]].
   * @param item the item to append.
   * @return the [[Description]] itself for fluent API.
   * @see [[DescItem]]
   * */
  def addItem[T <: DescItem](item: T): Description = { contents.addOne(item); this }

  /**
   * == Description.addItem ==
   *
   * Append a sequence of items into the [[Description]].
   *
   * @tparam T a subtype of [[DescItem]].
   * @param items the items to append.
   * @return the [[Description]] itself for fluent API.
   * @see [[DescItem]]
   * */
  def addItems[T <: DescItem](items: Seq[T]): Description = { items.foreach(i => contents.addOne(i)); this }

  override def toString: String = {
    if this.contents.isEmpty then "" else
      this.contents.map { _ match
        case DescItem.Header(c) => c.bold.underlined + endl
        case DescItem.Paragraph(c) => c
        case DescItem.UnorderedList(c) => c.map(" - " + _).reduce(_ + endl + _)
        case DescItem.OrderedList(c) => c.zipWithIndex.map(i => s" ${i._2 + 1}. " + i._1).reduce(_ + endl + _)
      }.reduce(_ + endl + _)
  }
}

object Description {
  /**
   * == Description.apply ==
   *
   * Construct an empty [[Description]].
   *
   * @return the [[Description]] instance.
   * */
  def apply(): Description = new Description()

  /**
   * == Description.apply ==
   *
   * Construct a [[Description]] with only 1 simple paragraph.
   *
   * @param content the paragraph content.
   * @return the [[Description]] instance.
   * */
  def apply(content: String): Description = new Description() += Paragraph(content)

  /**
   * == Description.apply ==
   *
   * Construct a [[Description]] with a sequence of items.
   *
   * @param items the item arg list.
   * @return the [[Description]] instance.
   * @see [[DescItem]]
   * */
  def apply(items: DescItem*): Description = new Description() ++= items

  /**
   * == Description.DescItem ==
   *
   * An ADT that represents the available item type of [[Description]].
   * */
  enum DescItem {
    case Header       (val content: String)       extends DescItem
    case Paragraph    (val content: String)       extends DescItem
    case UnorderedList(val content: Seq[String])  extends DescItem
    case OrderedList  (val content: Seq[String])  extends DescItem
  }

  object DescItem {
    /**
     * == DescItem.header ==
     *
     * Construct a [[Header]] instance.
     *
     * @param content the header content.
     * @return the instance.
     * @see [[Header]]
     * */
    inline def header(content: String): Header = Header(content)

    /**
     * == DescItem.para ==
     *
     * Construct a [[Paragraph]] instance.
     *
     * @param content the paragraph content.
     * @return the instance.
     * @see [[Paragraph]]
     * */
    inline def para(content: String): Paragraph = Paragraph(content)

    /**
     * == DescItem.uList ==
     *
     * Construct a [[UnorderedList]] instance.
     *
     * @param items the items of list.
     * @return the instance.
     * @see [[UnorderedList]]
     * */
    inline def uList(items: String*): UnorderedList = UnorderedList(items)

    /**
     * == DescItem.oList ==
     *
     * Construct a [[OrderedList]] instance.
     *
     * @param items the items of list.
     * @return the instance.
     * @see [[OrderedList]]
     * */
    inline def oList(items: String*): OrderedList = OrderedList(items)
  }

  export DescItem.{ header, para, uList, oList }                    // export `DescItem` builders.
  export DescItem.{ Header, Paragraph, UnorderedList, OrderedList } // export `DescItem` sub-types.
}

