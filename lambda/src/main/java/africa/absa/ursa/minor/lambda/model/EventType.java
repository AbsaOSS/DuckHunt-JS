/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package africa.absa.ursa.minor.lambda.model;
@org.apache.avro.specific.AvroGenerated
public enum EventType implements org.apache.avro.generic.GenericEnumSymbol<EventType> {
  SHOT, HIT, SCORE  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"EventType\",\"namespace\":\"africa.absa.absaoss.kafkarest.model\",\"symbols\":[\"SHOT\",\"HIT\",\"SCORE\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}
