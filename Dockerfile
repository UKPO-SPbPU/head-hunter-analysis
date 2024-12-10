FROM amazoncorretto:21-alpine
LABEL description="Образ для ручного запуска программ"

COPY ./scripts/run-parser.sh /usr/local/bin/run-parser
COPY ./scripts/run-mongo-loader.sh /usr/local/bin/run-mongo-loader
COPY ./scripts/run-elastic-loader.sh /usr/local/bin/run-elastic-loader
RUN chmod +x /usr/local/bin/run-parser /usr/local/bin/run-mongo-loader /usr/local/bin/run-elastic-loader

WORKDIR /app
COPY . .
RUN ./gradlew build

ENTRYPOINT ["bash"]
