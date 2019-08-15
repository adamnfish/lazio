import apigateway = require('@aws-cdk/aws-apigateway'); 
import lambda = require('@aws-cdk/aws-lambda');
import cdk = require('@aws-cdk/core');

export class LazioLambda extends cdk.Stack {
  constructor(app: cdk.App, id: string) {
    super(app, id);

    const lambdaFn = new lambda.Function(this, 'lazio-example', {
      code: lambda.Code.asset("../target/universal/example.zip"),
      handler: 'com.adamnfish.lazio.Lambda',
      runtime: lambda.Runtime.JAVA_8,
      memorySize: 1024,
      timeout: cdk.Duration.seconds(20),
      environment: {}
    });
    
    new apigateway.LambdaRestApi(this, 'lazio-api', {
      handler: lambdaFn,
      restApiName: 'Lazio example'
    });
  }
}

const app = new cdk.App();
new LazioLambda(app, 'LazioLambda');
