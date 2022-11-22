package stats.model

import com.outr.arango.{Document, DocumentModel, DocumentRef, Field, Id, Index}
import fabric.rw.RW
import stats.model

case class Employee(name: String,
                    salary: Int,
                    currency: String,
                    department: String,
                    sub_department: Option[String],
                    on_contract: Boolean = false,
                    _id: Id[Employee] = Employee.id()) extends Document[Employee]

object Employee extends DocumentModel[Employee] {
  override implicit val rw: RW[Employee] = RW.gen

  val name: Field[String] = field("name")
  val salary: Field[Int] = field("salary")
  val currency: Field[String] = field("currency")
  val department: Field[String] = field("department")
  val sub_department: Field[Option[String]] = field("sub_department")
  val on_contract: Field[Boolean] = field("on_contract")

  def ref: DocumentRef[Employee, model.Employee.type] = DocumentRef(this, Some("e"))

  override val collectionName: String = "employees"

  override def indexes: List[Index] = List(
    name.index.persistent(unique = true),   // Assuming this is unique since there is no other unique identifier
    salary.index.persistent(),
    currency.index.persistent(),
    department.index.persistent(),
    sub_department.index.persistent()
  )
}