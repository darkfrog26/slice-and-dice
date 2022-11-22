package stats.model

import com.outr.arango.{Document, DocumentModel, Field, Id, Index}
import fabric.rw.RW

case class Employee(name: String,
                    salary: Int,
                    currency: String,
                    department: String,
                    _id: Id[Employee] = Employee.id()) extends Document[Employee]

object Employee extends DocumentModel[Employee] {
  override implicit val rw: RW[Employee] = RW.gen

  val name: Field[String] = field("name")
  val salary: Field[Int] = field("salary")
  val currency: Field[String] = field("currency")
  val department: Field[String] = field("department")

  override val collectionName: String = "employees"

  override def indexes: List[Index] = List(
    name.index.persistent(unique = true),   // Assuming this is unique since there is no other unique identifier
    salary.index.persistent(),
    currency.index.persistent(),
    department.index.persistent()
  )
}