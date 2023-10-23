/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package africa.absa.ursa.minor.lambda.model;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;

/** Duck Hunt Event Schema. */
@org.apache.avro.specific.AvroGenerated
public class DuckHuntEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1235172988583611158L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DuckHuntEvent\",\"namespace\":\"africa.absa.absaoss.kafkarest.model\",\"doc\":\"Duck Hunt Event Schema.\",\"fields\":[{\"name\":\"email\",\"type\":\"string\",\"doc\":\"email address of event\"},{\"name\":\"eventType\",\"type\":{\"type\":\"enum\",\"name\":\"EventType\",\"symbols\":[\"SHOT\",\"HIT\",\"SCORE\"]},\"doc\":\"The type of the event.\"},{\"name\":\"eventSize\",\"type\":\"int\",\"doc\":\"The ssize of the event\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<DuckHuntEvent> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<DuckHuntEvent> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<DuckHuntEvent> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<DuckHuntEvent> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<DuckHuntEvent> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this DuckHuntEvent to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a DuckHuntEvent from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a DuckHuntEvent instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static DuckHuntEvent fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** email address of event */
  private CharSequence email;
  /** The type of the event. */
  private EventType eventType;
  /** The ssize of the event */
  private int eventSize;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public DuckHuntEvent() {}

  /**
   * All-args constructor.
   * @param email email address of event
   * @param eventType The type of the event.
   * @param eventSize The ssize of the event
   */
  public DuckHuntEvent(CharSequence email, EventType eventType, Integer eventSize) {
    this.email = email;
    this.eventType = eventType;
    this.eventSize = eventSize;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public Object get(int field$) {
    switch (field$) {
    case 0: return email;
    case 1: return eventType;
    case 2: return eventSize;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: email = (CharSequence)value$; break;
    case 1: eventType = (EventType)value$; break;
    case 2: eventSize = (Integer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'email' field.
   * @return email address of event
   */
  public CharSequence getEmail() {
    return email;
  }


  /**
   * Sets the value of the 'email' field.
   * email address of event
   * @param value the value to set.
   */
  public void setEmail(CharSequence value) {
    this.email = value;
  }

  /**
   * Gets the value of the 'eventType' field.
   * @return The type of the event.
   */
  public EventType getEventType() {
    return eventType;
  }


  /**
   * Sets the value of the 'eventType' field.
   * The type of the event.
   * @param value the value to set.
   */
  public void setEventType(EventType value) {
    this.eventType = value;
  }

  /**
   * Gets the value of the 'eventSize' field.
   * @return The ssize of the event
   */
  public int getEventSize() {
    return eventSize;
  }


  /**
   * Sets the value of the 'eventSize' field.
   * The ssize of the event
   * @param value the value to set.
   */
  public void setEventSize(int value) {
    this.eventSize = value;
  }

  /**
   * Creates a new DuckHuntEvent RecordBuilder.
   * @return A new DuckHuntEvent RecordBuilder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Creates a new DuckHuntEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new DuckHuntEvent RecordBuilder
   */
  public static Builder newBuilder(Builder other) {
    if (other == null) {
      return new Builder();
    } else {
      return new Builder(other);
    }
  }

  /**
   * Creates a new DuckHuntEvent RecordBuilder by copying an existing DuckHuntEvent instance.
   * @param other The existing instance to copy.
   * @return A new DuckHuntEvent RecordBuilder
   */
  public static Builder newBuilder(DuckHuntEvent other) {
    if (other == null) {
      return new Builder();
    } else {
      return new Builder(other);
    }
  }

  /**
   * RecordBuilder for DuckHuntEvent instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<DuckHuntEvent>
    implements org.apache.avro.data.RecordBuilder<DuckHuntEvent> {

    /** email address of event */
    private CharSequence email;
    /** The type of the event. */
    private EventType eventType;
    /** The ssize of the event */
    private int eventSize;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.email)) {
        this.email = data().deepCopy(fields()[0].schema(), other.email);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.eventType)) {
        this.eventType = data().deepCopy(fields()[1].schema(), other.eventType);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.eventSize)) {
        this.eventSize = data().deepCopy(fields()[2].schema(), other.eventSize);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing DuckHuntEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(DuckHuntEvent other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.email)) {
        this.email = data().deepCopy(fields()[0].schema(), other.email);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.eventType)) {
        this.eventType = data().deepCopy(fields()[1].schema(), other.eventType);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.eventSize)) {
        this.eventSize = data().deepCopy(fields()[2].schema(), other.eventSize);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'email' field.
      * email address of event
      * @return The value.
      */
    public CharSequence getEmail() {
      return email;
    }


    /**
      * Sets the value of the 'email' field.
      * email address of event
      * @param value The value of 'email'.
      * @return This builder.
      */
    public Builder setEmail(CharSequence value) {
      validate(fields()[0], value);
      this.email = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'email' field has been set.
      * email address of event
      * @return True if the 'email' field has been set, false otherwise.
      */
    public boolean hasEmail() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'email' field.
      * email address of event
      * @return This builder.
      */
    public Builder clearEmail() {
      email = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'eventType' field.
      * The type of the event.
      * @return The value.
      */
    public EventType getEventType() {
      return eventType;
    }


    /**
      * Sets the value of the 'eventType' field.
      * The type of the event.
      * @param value The value of 'eventType'.
      * @return This builder.
      */
    public Builder setEventType(EventType value) {
      validate(fields()[1], value);
      this.eventType = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'eventType' field has been set.
      * The type of the event.
      * @return True if the 'eventType' field has been set, false otherwise.
      */
    public boolean hasEventType() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'eventType' field.
      * The type of the event.
      * @return This builder.
      */
    public Builder clearEventType() {
      eventType = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'eventSize' field.
      * The ssize of the event
      * @return The value.
      */
    public int getEventSize() {
      return eventSize;
    }


    /**
      * Sets the value of the 'eventSize' field.
      * The ssize of the event
      * @param value The value of 'eventSize'.
      * @return This builder.
      */
    public Builder setEventSize(int value) {
      validate(fields()[2], value);
      this.eventSize = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'eventSize' field has been set.
      * The ssize of the event
      * @return True if the 'eventSize' field has been set, false otherwise.
      */
    public boolean hasEventSize() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'eventSize' field.
      * The ssize of the event
      * @return This builder.
      */
    public Builder clearEventSize() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DuckHuntEvent build() {
      try {
        DuckHuntEvent record = new DuckHuntEvent();
        record.email = fieldSetFlags()[0] ? this.email : (CharSequence) defaultValue(fields()[0]);
        record.eventType = fieldSetFlags()[1] ? this.eventType : (EventType) defaultValue(fields()[1]);
        record.eventSize = fieldSetFlags()[2] ? this.eventSize : (Integer) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<DuckHuntEvent>
    WRITER$ = (org.apache.avro.io.DatumWriter<DuckHuntEvent>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<DuckHuntEvent>
    READER$ = (org.apache.avro.io.DatumReader<DuckHuntEvent>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.email);

    out.writeEnum(this.eventType.ordinal());

    out.writeInt(this.eventSize);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.email = in.readString(this.email instanceof Utf8 ? (Utf8)this.email : null);

      this.eventType = EventType.values()[in.readEnum()];

      this.eventSize = in.readInt();

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.email = in.readString(this.email instanceof Utf8 ? (Utf8)this.email : null);
          break;

        case 1:
          this.eventType = EventType.values()[in.readEnum()];
          break;

        case 2:
          this.eventSize = in.readInt();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}









