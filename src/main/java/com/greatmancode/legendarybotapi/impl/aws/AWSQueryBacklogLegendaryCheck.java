package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.greatmancode.legendarybotapi.messages.MessageHelper;

public class AWSQueryBacklogLegendaryCheck implements RequestHandler<Void,Void> {

    private static AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion(System.getenv("AWS_REGION") != null ? System.getenv("AWS_REGION") : "us-east-1").build();

    @Override
    public Void handleRequest(Void input, Context context) {
        GetQueueUrlResult queueUrlResult = sqs.getQueueUrl(System.getenv("SQS_LEGENDARYCHECK_QUEUE"));
        GetQueueAttributesRequest queueAttributesRequest = new GetQueueAttributesRequest().withQueueUrl(queueUrlResult.getQueueUrl()).withAttributeNames("ApproximateNumberOfMessages");
        int messageCount = Integer.parseInt(sqs.getQueueAttributes(queueAttributesRequest).getAttributes().get("ApproximateNumberOfMessages"));
        if (messageCount > 0) {
            process(queueUrlResult, messageCount);
            messageCount = Integer.parseInt(sqs.getQueueAttributes(queueAttributesRequest).getAttributes().get("ApproximateNumberOfMessages"));
            if (messageCount > 0) {
                process(queueUrlResult, messageCount);
            }
        }
        return null;
    }

    private void process(GetQueueUrlResult queueUrlResult, int messageCount) {
        ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrlResult.getQueueUrl())
                .withMaxNumberOfMessages(Math.min(messageCount,10));
        sqs.receiveMessage(request).getMessages().forEach(message -> {
            MessageHelper.sendMessage(System.getenv("SNS_LEGENDARYCHECK"), message.getBody());
            sqs.deleteMessage(queueUrlResult.getQueueUrl(), message.getReceiptHandle());
        });
    }
}
