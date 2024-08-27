package com.example.travel_search.api.flight.domain.doc
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "flight-data")
class FlightDocument(
    @Id
    var id: String? = null,

    @Field(type = FieldType.Keyword)
    var date: String,

    @Field(type = FieldType.Text)
    var startCode: String,

    @Field(type = FieldType.Text)
    var endCode: String,

    @Field(type = FieldType.Nested)
    var data: List<Route>
) {

    class Route {
        @Field(type = FieldType.Text)
        var startRoute: String? = null

        @Field(type = FieldType.Text)
        var endRoute: String? = null

        @Field(type = FieldType.Text)
        var airlineName: String? = null

        @Field(type = FieldType.Double)
        var price: Int? = null
    }
}
