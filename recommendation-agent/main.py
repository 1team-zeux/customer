import os
from fastapi import FastAPI
from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor

OTLP_ENDPOINT = os.getenv("OTEL_EXPORTER_OTLP_ENDPOINT", "http://otel-collector:4318")
SERVICE_NAME  = os.getenv("OTEL_SERVICE_NAME", "recommendation-agent")

provider = TracerProvider()
provider.add_span_processor(
    BatchSpanProcessor(OTLPSpanExporter(endpoint=f"{OTLP_ENDPOINT}/v1/traces"))
)
trace.set_tracer_provider(provider)

app = FastAPI(title="Recommendation Agent", version="1.0.0")
FastAPIInstrumentor.instrument_app(app)

from routers.recommendations import router
app.include_router(router)

@app.get("/health")
def health():
    return {"status": "ok", "service": SERVICE_NAME}
